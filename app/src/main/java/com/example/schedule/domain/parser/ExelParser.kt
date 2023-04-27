package com.example.schedule.domain.parser

import com.example.schedule.data.model.Lesson
import com.example.schedule.data.model.LessonType
import com.example.schedule.domain.Resource
import org.apache.poi.ss.usermodel.*
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class ExelParser @Inject constructor() {
    private var parsedScheduleResult: Resource<Map<Int, MutableList<List<Lesson>>>> = Resource.Loading()
    private val platoonsWithSchedule = mutableMapOf<Int, MutableList<List<Lesson>>>()
    private val lessonTimeMap =
        mapOf(1 to "09:00 - 10:30", 2 to "10:40 - 12:10", 3 to "13:00 - 14:30", 4 to "14:40 - 16:10")

    fun getParsedData(): Resource<Map<Int, MutableList<List<Lesson>>>> {
        return parsedScheduleResult
    }

    fun parse(file: File) {
        parsedScheduleResult = Resource.Loading()
        platoonsWithSchedule.clear()
        try {
            val workbookStream = FileInputStream(file)
            val book = WorkbookFactory.create(workbookStream)
            parseBook(book)
        } catch (e: Exception) {
            parsedScheduleResult = Resource.Error(e.message ?: "parse Error")
            e.printStackTrace()
        }
    }

    private fun parseBook(book: Workbook) {
        if (book.numberOfSheets > 0) {
            var weekNumber = 0
            val sheet = book.getSheetAt(0)
            var curRowNumber = 5
            repeat(4) {// читаем каждый месяц (страницу)
                repeat(5) {//читаем каждый день недели
                    parseDay(curRowNumber, sheet, weekNumber)
                    curRowNumber += 14
                }
                curRowNumber += 6
                weekNumber += 4
            }
        }
        parsedScheduleResult = Resource.Success(platoonsWithSchedule)
    }

    private fun parseDay(curRowNumber: Int, sheet: Sheet, weekNumber: Int) {
        var curColNumber = 3
        var curWeekNumber = weekNumber
        val platoonRow = sheet.getRow(curRowNumber)
        for (i in 1 .. 16) {
            // парсим всю строку с 1 по 4 пару за один день
            val platoon = parsePlatoon(platoonRow, curColNumber)
            if (platoon == 0) {
                curColNumber += 2
                continue
            }
            val lessons =
                parseLessons(curRowNumber + 1, curColNumber, sheet, platoon, curWeekNumber)
            platoonsWithSchedule[platoon]?.add(lessons)
            curColNumber += 2
            if (i % 4 == 0) curWeekNumber++
        }
    }

    private fun parseLessons(
        RowStartNumber: Int,
        curColNumber: Int,
        sheet: Sheet,
        platoon: Int,
        curWeekNumber: Int
    ): List<Lesson> {
        var curRowNumber = RowStartNumber
        val lessons = mutableListOf<Lesson>()
        for (i in 0..3) {
            val lessonsName = parseLessonName(curRowNumber, curColNumber, sheet)
            if (lessonsName.isEmpty()) continue //если нет пары
            curRowNumber++
            val lessonRooms = parseLessonRoom(curRowNumber, curColNumber, sheet)
            curRowNumber++
            val teachers = parseTeacher(curRowNumber, curColNumber, sheet)
            curRowNumber++
            if (lessonRooms[1].isEmpty()) continue // если праздничный или выходной день
            val platoonLesson = Lesson(
                platoonNumber = platoon,
                lessonName = lessonsName,
                lessonType = lessonRooms[0].toLessonType(),
                classRoom = lessonRooms[1],
                mainTeacher = teachers[0],
                helpTeacher = teachers[1],
                lessonTime = lessonTimeMap[i + 1]!!,
                weekNumber = curWeekNumber + 1
            )
            lessons.add(platoonLesson)
        }
        return lessons
    }

    private fun parseTeacher(teacherRowNumber: Int, colNumber: Int, sheet: Sheet): List<String> {
        val result = mutableListOf<String>()
        val teacherRow = sheet.getRow(teacherRowNumber)
        repeat(2) {
            val teacherCell = teacherRow.getCell(colNumber + it)
            if (teacherCell.cellType != CellType.BLANK) {
                result.add(teacherCell.stringCellValue.trim())
            } else {
                result.add("")
            }
        }
        return result
    }

    private fun parseLessonRoom(roomRowNumber: Int, colNumber: Int, sheet: Sheet): List<String> {
        val result = mutableListOf<String>()
        val lessonRoomRow = sheet.getRow(roomRowNumber)
        repeat(2) {
            val lessonRoomCell = lessonRoomRow.getCell(colNumber + it)
            if (lessonRoomCell?.cellType != CellType.BLANK) {
                //есть прикол что одна ячейка может быть null
                result.add(lessonRoomCell?.stringCellValue?.trim()?.lowercase() ?: "")
            } else {
                result.add("")
            }
        }
        return result
    }

    private fun String.toLessonType(): LessonType {
        return when {
            this.endsWith("/л") -> LessonType.Lecture
            this.endsWith("/с") -> LessonType.Seminar
            this.endsWith("з") -> LessonType.Practice
            this.endsWith("зачет") -> LessonType.Test
            else -> LessonType.SelfStudy
        }
    }

    private fun parseLessonName(RowStartNumber: Int, colNumber: Int, sheet: Sheet): String {
        var result = ""
        val lessonNameRow = sheet.getRow(RowStartNumber)
        val lessonNameCell = lessonNameRow.getCell(colNumber)
        if (lessonNameCell.cellType != CellType.BLANK) {
            val lessonName = lessonNameCell.stringCellValue.trim()
            result = lessonName
        }
        return result
    }


    private fun parsePlatoon(platoonRow: Row, ColNumber: Int): Int {
        var result = 0
        val platoonCell = platoonRow.getCell(ColNumber)
        if (platoonCell.cellType != CellType.BLANK) {
            val platoon = platoonCell.stringCellValue.split(" ")[0].toInt()
            result = platoon
            if (platoonsWithSchedule[platoon] == null) {
                platoonsWithSchedule[platoon] = mutableListOf()
            }
        }
        return result
    }

}
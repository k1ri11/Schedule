package com.example.schedule.domain.parser

import android.content.Context
import android.util.Log
import com.example.schedule.domain.model.Lesson
import org.apache.poi.ss.usermodel.*
import java.io.File
import java.io.FileInputStream

class ExelParser(
    private val context: Context,
) {
    private val platoonsWithSchedule = mutableMapOf<Int, MutableList<List<Lesson>>>()

    fun parse() {
        val file = File(context.filesDir, "shedule_vuc.xlsx")
        file.let {
            try {
                val workbookStream = FileInputStream(it)
                val book = WorkbookFactory.create(workbookStream)
                parseBook(book)
            } catch (e: Exception) {
                Log.d("SCHEDULE", "getExelFile: ${e.message} ")
                e.printStackTrace()
            }
        }
    }

    private fun parseBook(book: Workbook) {
        if (book.numberOfSheets > 0) {
            val sheet = book.getSheetAt(0)
            var curRowNumber = 5
            repeat(4) {// читаем каждый месяц (страницу)
                repeat(5) {//читаем каждый день недели
                    parseDay(curRowNumber, sheet)
                    curRowNumber += 14
                }
                curRowNumber += 6
            }
        }
        print(platoonsWithSchedule)
    }

    private fun parseDay(curRowNumber: Int, sheet: Sheet) {
        var curColNumber = 3
        val platoonRow = sheet.getRow(curRowNumber)
        for (i in 0 until 16) { // парсим всю строку с 1 по 4 пару за один день
            val platoon = parsePlatoon(platoonRow, curColNumber)
            if (platoon == 0) {
                curColNumber += 2
                continue
            }
            val lessons = parseLessons(curRowNumber + 1, curColNumber, sheet)
            platoonsWithSchedule[platoon]?.add(lessons)
            curColNumber += 2
        }
    }

    private fun parseLessons(
        RowStartNumber: Int,
        curColNumber: Int,
        sheet: Sheet,
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
                name = lessonsName,
                classRoomDesc = lessonRooms[0],
                classRoom = lessonRooms[1],
                teacher1 = teachers[0],
                teacher2 = teachers[1]
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
                result.add(lessonRoomCell?.stringCellValue?.trim() ?: "")
            } else {
                result.add("")
            }
        }
        return result
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
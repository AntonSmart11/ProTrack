package com.antonsmart.protrack.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.antonsmart.protrack.objects.Note
import com.antonsmart.protrack.objects.Project
import com.antonsmart.protrack.objects.Reminder
import com.antonsmart.protrack.objects.Role
import com.antonsmart.protrack.objects.User
import com.antonsmart.protrack.objects.Work
import kotlin.Exception
import kotlin.collections.ArrayList

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "protrack.db"

        //Table of users
        private const val TABLE_USERS = "users"
        private const val ID_USER = "id_users"
        private const val NAME_USER = "name"
        private const val LAST_USER = "last"
        private const val USERNAME_USER = "username"
        private const val PASSWORD_USER = "password"
        
        //Table of projects
        private const val TABLE_PROJECTS = "projects"
        private const val ID_PROJECT = "id_project"
        private const val ID_USER_PROJECT = "id_user"
        private const val TITLE_PROJECT = "title"
        private const val DATE_START_PROJECT = "date_start"
        private const val DATE_END_PROJECT = "date_end"
        private const val DESCRIPTION_PROJECT = "description"

        //Table of works
        private const val TABLE_WORKS = "works"
        private const val ID_WORK = "id_work"
        private const val ID_PROJECT_WORK = "id_project"
        private const val ID_USER_WORK = "id_user"
        private const val TITLE_WORK = "title"
        private const val DESCRIPTION_WORK = "description"
        private const val DATE_START_WORK = "date_start"
        private const val DATE_END_WORK = "date_end"
        private const val PERSON_WORK = "person"
        private const val ROLE_WORK = "role"
        private const val FINISH_WORK = "finish"

        //Table of roles
        private const val TABLE_ROLES = "roles"
        private const val ID_ROLE = "id_roles"
        private const val ID_USER_ROLE = "id_user"
        private const val NAME_ROLE = "name"

        //Table of notes
        private const val TABLE_NOTES = "notes"
        private const val ID_NOTE = "id_note"
        private const val ID_USER_NOTE = "id_user"
        private const val ID_WORK_NOTE = "id_work"
        private const val TITLE_NOTE = "title"
        private const val DESCRIPTION_NOTE = "description"

        //Table of reminders
        private const val TABLE_REMINDERS = "reminders"
        private const val ID_REMINDER = "id_reminder"
        private const val ID_USER_REMINDER = "id_user"
        private const val ID_PROJECT_REMINDER = "id_project"
        private const val WORK_REMINDER = "work"
        private const val TITLE_REMINDER = "title"
        private const val DATE_REMINDER = "date"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val TABLE_USERS = ("CREATE TABLE " + TABLE_USERS + "(" +
                ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_USER + " TEXT," +
                LAST_USER + " TEXT," +
                USERNAME_USER + " TEXT," +
                PASSWORD_USER + " TEXT"
                +")")

        val TABLE_PROJECTS = ("CREATE TABLE " + TABLE_PROJECTS + "(" +
                ID_PROJECT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_USER_PROJECT + " INTEGER," +
                TITLE_PROJECT + " TEXT," +
                DATE_START_PROJECT + " DATE," +
                DATE_END_PROJECT + " DATE," +
                DESCRIPTION_PROJECT + " TEXT"
                +")")

        val TABLE_WORKS = ("CREATE TABLE " + TABLE_WORKS + "(" +
                ID_WORK + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_PROJECT_WORK + " INTEGER," +
                ID_USER_WORK + " INTEGER," +
                TITLE_WORK + " TEXT," +
                DATE_START_WORK + " DATE," +
                DATE_END_WORK + " DATE," +
                DESCRIPTION_WORK + " TEXT," +
                PERSON_WORK + " TEXT," +
                ROLE_WORK + " TEXT," +
                FINISH_WORK + " TEXT"
                +")")

        val TABLE_ROLES = ("CREATE TABLE " + TABLE_ROLES + "("+
                ID_ROLE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_USER_ROLE + " INTEGER," +
                NAME_ROLE + " TEXT"
                +")")

        val TABLE_NOTES = ("CREATE TABLE "+ TABLE_NOTES + "("+
                ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_USER_NOTE + " INTEGER," +
                ID_WORK_NOTE + " INTEGER," +
                TITLE_NOTE + " TEXT," +
                DESCRIPTION_NOTE + " TEXT"
                +")")

        val TABLE_REMINDERS = ("CREATE TABLE "+ TABLE_REMINDERS + "("+
                ID_REMINDER + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ID_USER_REMINDER + " INTEGER," +
                ID_PROJECT_REMINDER + " INTEGER," +
                WORK_REMINDER + " TEXT," +
                TITLE_REMINDER + " TEXT," +
                DATE_REMINDER + " DATE"
                +")")

        db?.execSQL(TABLE_USERS)
        db?.execSQL(TABLE_PROJECTS)
        db?.execSQL(TABLE_WORKS)
        db?.execSQL(TABLE_ROLES)
        db?.execSQL(TABLE_NOTES)
        db?.execSQL(TABLE_REMINDERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PROJECTS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_WORKS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ROLES")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDERS")
        onCreate(db)
    }

    //Functions
    //User
    fun InsertUser(user: User): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME_USER, user.name)
        contentValues.put(LAST_USER, user.last)
        contentValues.put(USERNAME_USER, user.username)
        contentValues.put(PASSWORD_USER, user.password)

        val success = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun GetAllUsers(): ArrayList<User> {
        val userList: ArrayList<User> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USERS"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id_user: Int
        var name: String
        var last: String
        var username: String
        var password: String

        if(cursor.moveToFirst()) {
            do {
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER))
                name = cursor.getString(cursor.getColumnIndex(NAME_USER))
                last = cursor.getString(cursor.getColumnIndex(LAST_USER))
                username = cursor.getString(cursor.getColumnIndex(USERNAME_USER))
                password = cursor.getString(cursor.getColumnIndex(PASSWORD_USER))

                val user = User(id_user, name, last, username, password)
                userList.add(user)
            } while (cursor.moveToNext())
        }

        return userList
    }

    fun UpdateUser(user: User): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER, user.id)
        contentValues.put(NAME_USER, user.name)
        contentValues.put(LAST_USER, user.last)
        contentValues.put(USERNAME_USER, user.username)
        contentValues.put(PASSWORD_USER, user.password)

        val success = db.update(TABLE_USERS, contentValues, ID_USER + "=" + user.id, null)
        db.close()

        return success
    }

    fun DeleteUser(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER, id)

        val success = db.delete(TABLE_USERS, ID_USER + "=" + id, null)
        db.close()

        return success
    }

    //Project
    fun InsertProject(project: Project): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_PROJECT, project.id_user)
        contentValues.put(TITLE_PROJECT, project.title)
        contentValues.put(DATE_START_PROJECT, project.date_start)
        contentValues.put(DATE_END_PROJECT, project.date_end)
        contentValues.put(DESCRIPTION_PROJECT, project.description)

        val success = db.insert(TABLE_PROJECTS, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun GetAllProjects(): ArrayList<Project> {
        val projectList: ArrayList<Project> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_PROJECTS"
        val db = this.writableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id_project: Int
        var id_user: Int
        var title: String
        var date_start: String
        var date_end: String
        var description: String

        if(cursor.moveToFirst()) {
            do {
                id_project = cursor.getInt(cursor.getColumnIndex(ID_PROJECT))
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER_PROJECT))
                title = cursor.getString(cursor.getColumnIndex(TITLE_PROJECT))
                date_start = cursor.getString(cursor.getColumnIndex(DATE_START_PROJECT))
                date_end = cursor.getString(cursor.getColumnIndex(DATE_END_PROJECT))
                description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_PROJECT))

                val project = Project(id_project, id_user, title, date_start, date_end, description)
                projectList.add(project)
            } while (cursor.moveToNext())
        }

        return projectList
    }

    fun UpdateProject(project: Project): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_PROJECT, project.id)
        contentValues.put(ID_USER_PROJECT, project.id_user)
        contentValues.put(TITLE_PROJECT, project.title)
        contentValues.put(DATE_START_PROJECT, project.date_start)
        contentValues.put(DATE_END_PROJECT, project.date_end)
        contentValues.put(DESCRIPTION_PROJECT, project.description)

        val success = db.update(TABLE_PROJECTS, contentValues, ID_PROJECT + "=" + project.id, null)
        db.close()

        return success
    }

    fun DeleteProject(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_PROJECT, id)

        val success = db.delete(TABLE_PROJECTS, ID_PROJECT + "=" + id, null)
        db.close()

        return success
    }

    fun DeleteProjectUser(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_PROJECT, id)

        val success = db.delete(TABLE_PROJECTS, ID_USER_PROJECT + "=" + id, null)
        db.close()

        return success
    }

    //Works
    fun InsertWork(work: Work): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_PROJECT_WORK, work.id_project)
        contentValues.put(ID_USER_WORK, work.id_user)
        contentValues.put(TITLE_WORK, work.title)
        contentValues.put(DESCRIPTION_WORK, work.description)
        contentValues.put(DATE_START_PROJECT, work.date_start)
        contentValues.put(DATE_END_WORK, work.date_end)
        contentValues.put(PERSON_WORK, work.person)
        contentValues.put(ROLE_WORK, work.role)
        contentValues.put(FINISH_WORK, work.finish)

        val success = db.insert(TABLE_WORKS, null, contentValues)
        db.close()

        return success
    }

    @SuppressLint("Range")
    fun GetAllWorks(): ArrayList<Work> {
        val workList: ArrayList<Work> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_WORKS"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id_work: Int
        var id_project: Int
        var id_user: Int
        var title: String
        var description: String
        var date_start: String
        var date_end: String
        var person: String
        var role: String
        var finish: String

        if(cursor.moveToFirst()) {
            do {
                id_work = cursor.getInt(cursor.getColumnIndex(ID_WORK))
                id_project = cursor.getInt(cursor.getColumnIndex(ID_PROJECT_WORK))
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER_WORK))
                title = cursor.getString(cursor.getColumnIndex(TITLE_WORK))
                description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_WORK))
                date_start = cursor.getString(cursor.getColumnIndex(DATE_START_WORK))
                date_end = cursor.getString(cursor.getColumnIndex(DATE_END_WORK))
                person = cursor.getString(cursor.getColumnIndex(PERSON_WORK))
                role = cursor.getString(cursor.getColumnIndex(ROLE_WORK))
                finish = cursor.getString(cursor.getColumnIndex(FINISH_WORK))

                val work = Work(id_work, id_project, id_user, title, description, date_start, date_end, person, role, finish)
                workList.add(work)
            } while (cursor.moveToNext())
        }

        return workList
    }

    fun UpdateWork(work: Work): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_WORK, work.id)
        contentValues.put(ID_PROJECT_WORK, work.id_project)
        contentValues.put(ID_USER_WORK, work.id_user)
        contentValues.put(TITLE_WORK, work.title)
        contentValues.put(DESCRIPTION_WORK, work.description)
        contentValues.put(DATE_START_WORK, work.date_start)
        contentValues.put(DATE_END_WORK, work.date_end)
        contentValues.put(PERSON_WORK, work.person)
        contentValues.put(ROLE_WORK, work.role)
        contentValues.put(FINISH_WORK, work.finish)

        val success = db.update(TABLE_WORKS, contentValues, ID_WORK + "=" + work.id, null)
        db.close()

        return success
    }

    fun UpdateWorkFinish(id: Int, finish: String): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(FINISH_WORK, finish)

        val success = db.update(TABLE_WORKS, contentValues, ID_WORK + "=" + id, null)
        db.close()

        return success
    }

    fun DeleteWork(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_WORK, id)

        val success = db.delete(TABLE_WORKS, ID_WORK + "=" + id, null)
        db.close()

        return success
    }

    fun DeleteWorkProject(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_PROJECT_WORK, id)

        val success = db.delete(TABLE_WORKS, ID_PROJECT_WORK + "=" + id, null)
        db.close()

        return success
    }

    fun DeleteWorkUser(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_WORK, id)

        val success = db.delete(TABLE_WORKS, ID_USER_WORK + "=" + id, null)
        db.close()

        return success
    }


    //Role
    fun InsertRole(role: Role): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_PROJECT, role.id_user)
        contentValues.put(NAME_ROLE,role.name)

        val success = db.insert(TABLE_ROLES, null, contentValues)
        db.close()
        return success
    }


    @SuppressLint("Range")
    fun GetAllRoles(): ArrayList<Role> {
        val roleList: ArrayList<Role> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_ROLES"
        val db = this.writableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var id_user : Int
        var name : String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID_ROLE))
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER_ROLE))
                name = cursor.getString(cursor.getColumnIndex(NAME_ROLE))

                val role = Role(id, id_user, name)
                roleList.add(role)

            } while (cursor.moveToNext())
        }

        return roleList
    }


    fun UpdateRole(role: Role): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_ROLE,role.id)
        contentValues.put(ID_USER_ROLE,role.id_user)
        contentValues.put(NAME_ROLE,role.name)

        val success = db.update(TABLE_ROLES,contentValues, ID_ROLE + "=" + role.id,null)
        db.close()

        return success
    }

    fun DeleteRole(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_ROLE, id)

        val success = db.delete(TABLE_ROLES, ID_ROLE + "=" + id, null)
        db.close()

        return success
    }

    fun DeleteRoleUser(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_ROLE, id)

        val success = db.delete(TABLE_ROLES, ID_USER_ROLE + "=" + id, null)
        db.close()

        return success
    }


    //Notas
    fun InsertNote(note: Note): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_NOTE,note.id_user)
        contentValues.put(ID_WORK_NOTE,note.id_work)
        contentValues.put(TITLE_NOTE,note.title)
        contentValues.put(DESCRIPTION_NOTE,note.description)

        val success = db.insert(TABLE_NOTES, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun GetAllNotes(): ArrayList<Note> {
        val noteList: ArrayList<Note> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NOTES"
        val db = this.writableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var id_user : Int
        var id_work : Int
        var title : String
        var description : String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID_NOTE))
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER_NOTE))
                id_work = cursor.getInt(cursor.getColumnIndex(ID_WORK_NOTE))
                title = cursor.getString(cursor.getColumnIndex(TITLE_NOTE))
                description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTE))

                val note = Note(id, id_user, id_work, title, description)
                noteList.add(note)

            } while (cursor.moveToNext())
        }

        return noteList
    }

    fun UpdateNote(note: Note): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_NOTE,note.id_user)
        contentValues.put(ID_WORK_NOTE,note.id_work)
        contentValues.put(TITLE_NOTE,note.title)
        contentValues.put(DESCRIPTION_NOTE,note.description)

        val success = db.update(TABLE_NOTES,contentValues, ID_NOTE + "=" + note.id,null)
        db.close()

        return success
    }

    fun DeleteNote(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_NOTE, id)

        val success = db.delete(TABLE_NOTES, ID_NOTE + "=" + id, null)
        db.close()

        return success
    }

    //Reminder
    fun InsertReminder(reminder: Reminder): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER_REMINDER,reminder.id_user)
        contentValues.put(ID_PROJECT_REMINDER,reminder.id_project)
        contentValues.put(WORK_REMINDER,reminder.work)
        contentValues.put(TITLE_REMINDER,reminder.title)
        contentValues.put(DATE_REMINDER,reminder.date)

        val success = db.insert(TABLE_REMINDERS, null, contentValues)
        db.close()
        return success


        ///creación de recordatorio
    }

    @SuppressLint("Range")
    fun GetAllReminders(): ArrayList<Reminder> {
        val reminderList: ArrayList<Reminder> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_REMINDERS"
        val db = this.writableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var id_user : Int
        var id_project : Int
        var work : String
        var title : String
        var date : String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(ID_REMINDER))
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER_REMINDER))
                id_project = cursor.getInt(cursor.getColumnIndex(ID_PROJECT_REMINDER))
                work = cursor.getString(cursor.getColumnIndex(WORK_REMINDER))
                title = cursor.getString(cursor.getColumnIndex(TITLE_REMINDER))
                date = cursor.getString(cursor.getColumnIndex(DATE_REMINDER))

                val reminder = Reminder(id,title, id_user, id_project, work, date)
                reminderList.add(reminder)

            } while (cursor.moveToNext())
        }

        return reminderList
    }

    fun UpdateReminder(reminder: Reminder): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_REMINDER,reminder.id)
        contentValues.put(ID_USER_REMINDER,reminder.id_user)
        contentValues.put(ID_PROJECT_REMINDER,reminder.id_project)
        contentValues.put(WORK_REMINDER,reminder.work)
        contentValues.put(TITLE_REMINDER,reminder.title)
        contentValues.put(DATE_REMINDER,reminder.date)

        val success = db.update(TABLE_REMINDERS,contentValues, ID_REMINDER + "=" + reminder.id,null)
        db.close()

        return success
    }

    fun DeleteReminder(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_REMINDER, id)

        val success = db.delete(TABLE_REMINDERS, ID_REMINDER + "=" + id, null)
        db.close()

        return success
    }

}
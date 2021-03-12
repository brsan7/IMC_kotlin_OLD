package com.brsan7.imc.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.helper.HelperDB

class ImcContentProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper: HelperDB

    override fun onCreate(): Boolean {
        mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTORITY, "registros", REGISTROS)
        mUriMatcher.addURI(AUTORITY, "registros/#", REGISTROS_BY_ID)
        if(context != null){dbHelper = HelperDB(context as Context)
        }
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        /*if(mUriMatcher.match(uri) == REGISTROS_BY_ID){
            //HistoricoApplication.instance.helperDB?.deletarRegistro(index)
            context?.contentResolver?.notifyChange(uri,null)
            return 1//linesAfecct
        }
        else{
            throw UnsupportedSchemeException("Uri inválida para exclusão")
        }*/
        return 0
    }

    override fun getType(uri: Uri): String? = throw UnsupportedSchemeException("Uri não implementada")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val insertUri:Uri = Uri.withAppendedPath(BASE_URI, "id.toString()")
        return insertUri
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when{
            mUriMatcher.match(uri) == REGISTROS->{
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor = db.query(HistoricoApplication.instance.helperDB?.TABLE_NAME,
                projection,selection,selectionArgs,null,null,sortOrder)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            mUriMatcher.match(uri) == REGISTROS_BY_ID->{
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor = db.query(HistoricoApplication.instance.helperDB?.TABLE_NAME,
                        projection,"$_ID = ?", arrayOf(uri.lastPathSegment),null,null,sortOrder)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else->{
                throw UnsupportedSchemeException("Uri não implementada")
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    companion object{
        const val AUTORITY = "com.brsan7.imc.provider"
        val BASE_URI = Uri.parse("content://$AUTORITY")
        val URI_REGISTROS = Uri.withAppendedPath(BASE_URI,"registros")
        const val REGISTROS =1
        const val REGISTROS_BY_ID = 2
    }
}
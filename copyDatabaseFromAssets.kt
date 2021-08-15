package org.techtown.testrecyclerview

import android.content.Context
import java.io.*

private val DB_PATH = "/data/data/org.techtown.testrecyclerview/databases/"
private val DB_NAME = "food_nutri.db"

private fun copyDataBaseFromAssets(context: Context) {

    var myInput: InputStream? = null
    var myOutput: OutputStream? = null
    try {

        val folder = context.getDatabasePath("databases")


        if (!folder.exists())
            if (folder.mkdirs()) folder.delete()

        myInput = context.assets.open("databases/$DB_NAME")

        val outFileName = DB_PATH + DB_NAME

        val f = File(outFileName)


        if (f.exists())
            return

        myOutput = FileOutputStream(outFileName)

        //transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int = myInput.read(buffer)

        while (length > 0) {
            myOutput!!.write(buffer, 0, length)
            length = myInput.read(buffer)
        }

        //Close the streams
        myOutput!!.flush()
        myOutput.close()
        myInput.close()


    } catch (e: IOException) {
        e.printStackTrace()
    }

}
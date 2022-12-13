package kr.or.mrhi.cinemastorage.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class ReviewDAO {
    /*firebaseRealtimeDatabase reviewTbl */
    var databaseReference: DatabaseReference? = null

    init {
        /*get instance reviewTbl @realtimeDB */
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("review")
    }

    /*select reviewTbl @realtimeDB*/
    fun selectReview(): Query? {
        return databaseReference
    }

    /*delete reviewTbl @realtimeDB*/
    fun deleteReview(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}
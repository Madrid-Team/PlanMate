package data

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.dto.project.ProjectDto
import kotlinx.coroutines.flow.toList
import org.bson.Document

class CopyCollectionIfDifferentToTest(val database: MongoDatabase)  {

    val original = database.getCollection<Document>("projects")
    val copy = database.getCollection<Document>("projects_test")


     suspend fun copyCollectionIfDifferent(): MongoCollection<ProjectDto> {
        val originalDocs = original.find().toList()
        val copyDocs = copy.find().toList()
        val areStructurallyEqual = areStructurallyEqual(originalDocs, copyDocs)

        if (!areStructurallyEqual || originalDocs.size != copyDocs.size) {
            copy.deleteMany(Document()) // Clear the existing test collection
            if (originalDocs.isNotEmpty()) {
                copy.insertMany(originalDocs)
                println("projects_test collection refreshed.")

                return   database.getCollection<ProjectDto>("projects_test")
            } else {
                println("Original collection is empty. Nothing to copy.")
                throw AssertionError("Original collection is empty Can't Copy collection to do testing.")
              //  return false
            }
        } else {
            println("No structural difference. Copy not needed.")
            return database.getCollection<ProjectDto>("projects_test")
        }

    }

    private fun areStructurallyEqual(originalDocs: List<Document>, copyDocs: List<Document>): Boolean {

        val areStructurallyEqual = originalDocs.zip(copyDocs).all { (doc1, doc2) ->
            val keys1 = doc1.keys.filterNot { it == "_id" }.toSet()
            val keys2 = doc2.keys.filterNot { it == "id" }.toSet()

            keys1 == keys2 && doc1.size == doc2.size
        }
        return areStructurallyEqual
    }

}
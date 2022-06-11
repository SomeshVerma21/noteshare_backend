package com.gamest.notebook.notes.models

data class LoadFile(
     val filename:String,
     val fileType:String,
     val fileSize:String,
     val file:ByteArray
) {
     override fun equals(other: Any?): Boolean {
          if (this === other) return true
          if (javaClass != other?.javaClass) return false

          other as LoadFile

          if (filename != other.filename) return false
          if (fileType != other.fileType) return false
          if (fileSize != other.fileSize) return false
          if (!file.contentEquals(other.file)) return false

          return true
     }

     override fun hashCode(): Int {
          var result = filename.hashCode()
          result = 31 * result + fileType.hashCode()
          result = 31 * result + fileSize.hashCode()
          result = 31 * result + file.contentHashCode()
          return result
     }
}

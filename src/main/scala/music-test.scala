import org.apache.spark.mllib.recommendation._
import org.apache.spark.{SparkConf, SparkContext}
import scala.tools._

object Music extends App {
	//Minimum number of partitions to make 
   val minPartitions: Int = 8
    val conf = new SparkConf()
        .setAppName("Music Recommender")
        .setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)
   
    
	//Load User Artist Data
	val rawUserArtist = sc.textFile("file:///E:/Minor/src/main/resources/user_artist_data_small.txt", minPartitions)
    rawUserArtist.persist()

	//Load Artist Data and Parse it
    val rawArtist = sc.textFile("file:///E:/Minor/src/main/resources/artist_data_small.txt", minPartitions)
    var artistByID = rawArtist.flatMap { line =>
        val (id, name) = line.span(_ != '\t')
        if (name.isEmpty) {
            None
        } else {
            try {
                Some((id.toInt, name.trim))
            } catch {
                case e: NumberFormatException => None
            }
        }
    }
    artistByID.persist()

   // Load Artist Alias and Parse it
    val rawArtistAlias = sc.textFile("file:///E:/Minor/src/main/resources/artist_alias_small.txt", minPartitions)
    var artistAlias = rawArtistAlias.flatMap { line =>
        val tokens = line.split("\t")
        if (tokens(0).isEmpty) {
            None
        } else {
            Some((tokens(0).toInt, tokens(1).toInt))
        }
    }.collectAsMap()
    val bArtistAlias = sc.broadcast(artistAlias)

			//Training Data
            val trainData = rawUserArtist.map { line =>
            val Array(userID, artistID, count) = line.split(" ").map(_.toInt)
         
            val id = bArtistAlias.value.getOrElse(artistID, artistID)
            val r = Rating(userID, id, count)
          
            r
        }.cache()

        //println("Train data size ${trainData.count()}")

        //println("Training model")
        
		
		val model = ALS.train(trainData, 10, 5, 1.0, 1)

        //println("Saving model")
    //Saves 5 recommendations for every user to the path 
	model.recommendProductsForUsers(5).mapValues(_.toVector).saveAsTextFile("file:///E:/Minor/Output")
    
sc.stop()
}



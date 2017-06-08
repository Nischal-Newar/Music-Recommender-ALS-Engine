# Music-Recommender-ALS-Engine
Use the Simple ALS algorithm to create a Music Recommender Engine over Apache Spark


The Project uses Scala Language to create a simple Model to provide Recommendations to users based on their Play Count.

Music Listening Dataset Audioscrobbler.com 6 May 2005

This data set contains profiles for around 150,000 real people The dataset lists the artists each person listens to, and a counter indicating how many times each user played each artist

The dataset is continually growing; at the time of writing (6 May 2005) Audioscrobbler is receiving around 2 million song submissions per day

We may produce additional/extended data dumps if anyone is interested in experimenting with the data.

The Data Taken from the above source has been trimmed down to make it easier and quicker to process. The output saves out top 5 recommendations for each user listed in the data.

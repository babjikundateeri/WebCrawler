Welcome to the WebCrawler wiki!

* Building WebCrawler

     mvn package

* Running WebCrawler

     java -jar WebCrawler.java [YEAR] [Months] [folder]

  Options
  * YEAR - It will be the year number in YYYY format, by default it is 2016
  * Months - It will be the Month(s) in MMM or ALL, by default it takes ALL
           - we can give months names in coma separated list.
  * folder - It will be the MailArchives folder name, by default it will be maven-users

ex : 
java -jar WebCrawler.java 2016 Jan,Feb maven-users
  This Job will run for the archives of maven-users of the year 2016 in the months of January and February
java -jar WebCrawler.java 2014 All incubator-iota-dev
  This Job will run for the archives of incubator-iota-dev for the year 2014

# ds_annon

## DB CREATE COMMAND (MySQL)

- User Table

CREATE TABLE user_table (
id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
name VARCHAR(200) NOT NULL,
pass VARCHAR(200) NOT NULL,
PRIMARY KEY(id)
);

* * *

- Annotation Data

CREATE TABLE annon_beginner (
id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
prop VARCHAR(50) NOT NULL,
sbj VARCHAR(200) NOT NULL,
obj VARCHAR(200) NOT NULL,
stc VARCHAR(8000) NOT NULL,
stc2 VARCHAR(8000) NOT NULL,
answer VARCHAR(10),
name VARCHAR(20),
locked BOOLEAN NOT NULL DEFAULT 0,
evi_str VARCHAR(1000),
evi_start_offset VARCHAR(20),
evi_end_offset VARCHAR(20),
PRIMARY KEY(id)
) DEFAULT CHARACTER SET utf8 collate utf8_general_ci;

* * *

- Note that both subject and object of triple are marked by '(sbj)' and '(/obj)' in stc field.
  Example of annon data
  
    |  1 | previousWork | http://ko.dbpedia.org/resource/신_(소설)    | http://ko.dbpedia.org/resource/천사들의_제국       |  그가 등장하는 소설은 《타나토노트》, 《(obj)천사들의_제국(/obj)》,《(sbj)신_(소설)(/sbj)》 등이 있다.                                           | -1     | com7468   |      1 | undefined                                                   | -1               | -1             |

    |  2 | country      | http://ko.dbpedia.org/resource/여수시       | http://ko.dbpedia.org/resource/대한민국            | 신풍역(新豊驛)은 (obj)대한민국(/obj) 전라남도 (sbj)여수시(/sbj) 율촌면 신풍리에 위치하였던 전라선의 역이다.                                      | 1      | powellite |      1 |  (obj)대한민국(/obj) 전라남도 (sbj)여수시(/sbj)             | 9                | 46             |
    
    
    
## Server Side Setting
1. MySQL Connection
 - In Configuration.java
  - dbAddr : Server IP / Database Name
  - dbUser : MySQL root id
  - dbPass : MySQL root pw
  
2. REST API
 - In AnnonMain.java
  - Line 29 : please set your own port number (ex, 18503)
 
3. DB Table
 - In FeedbackManager.java
  - Replace all database names in the sql query. (ex, annon_advanced --> something)

> This work was supported by Institute for In-formation & communications Technology Promotion(IITP) grant funded by the Korea government(MSIP) (2013-0-00109, WiseKB: Big data based self-evolving knowledge base and reasoning platform)

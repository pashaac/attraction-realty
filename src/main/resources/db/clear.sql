DELETE FROM venue;

DROP TABLE venue;


SELECT address, rating, title from venue where address like '%null%';

SELECT AVG(RATING) from venue WHERE city = 2;
SELECT AVG(RATING) from venue WHERE city = 1;
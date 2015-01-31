 -- For cmpt371 project: EatSafe 
 -- this file copy the tables and records out from temp database to files

\copy location TO 'location';
\copy inspection TO 'inspection';
\copy violation_types TO 'violation_types';
\copy violations TO 'violations';
\copy rating TO 'rating';
\copy categories TO 'categories';
\copy category_description TO 'category_description';


 -- For cmpt371 project: EatSafe 
 -- this file copy the tables and records out to database from files

\copy location FROM 'location';
\copy inspection FROM 'inspection';
\copy violation_types FROM 'violation_types';
\copy violations FROM 'violations';
\copy rating FROM 'rating';
\copy categories FROM 'categories';
\copy category_description FROM 'category_description';


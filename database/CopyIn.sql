 -- EatSafe 
 -- copy the tables and records to database from files

\copy location FROM 'location';
\copy inspection FROM 'inspection';
\copy violation_types FROM 'violation_types';
\copy violations FROM 'violations';


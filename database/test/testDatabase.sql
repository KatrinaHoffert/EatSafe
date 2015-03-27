INSERT INTO violation_type (id, description, name,  priority)
VALUES (1, 'parser.desc.1', 'parser.name.1', 'parser.priority.critical'),
(2, 'parser.desc.2', 'parser.name.2', 'parser.priority.critical'),
(3, 'parser.desc.3', 'parser.name.3', 'parser.priority.critical'),
(4, 'parser.desc.4', 'parser.name.4', 'parser.priority.critical'),
(5, 'parser.desc.5', 'parser.name.5', 'parser.priority.critical'),
(6, 'parser.desc.6', 'parser.name.6', 'parser.priority.critical'),
(7, 'parser.desc.7', 'parser.name.7', 'parser.priority.critical'),
(8, 'parser.desc.8', 'parser.name.8', 'parser.priority.critical'),
(9, 'parser.desc.9', 'parser.name.9', 'parser.priority.critical'),
(10, 'parser.desc.10', 'parser.name.10', 'parser.priority.general'),
(11, 'parser.desc.11', 'parser.name.11', 'parser.priority.general'),
(12, 'parser.desc.12', 'parser.name.12', 'parser.priority.general'),
(13, 'parser.desc.13', 'parser.name.13', 'parser.priority.general'),
(14, 'parser.desc.14', 'parser.name.14', 'parser.priority.general'),
(15, 'parser.desc.15', 'parser.name.15', 'parser.priority.general'),
(16, 'parser.desc.16', 'parser.name.16', 'parser.priority.general');


INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (1, 'Andrews Kitchen', 'My Place', 'S0N 0H0', 'Town A', 'A Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (1, 1, '2014-12-17', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (2, 1, '2013-12-17', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (3, 1, '2012-12-17', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (2, 'Andrews Bathroom', 'My Place', 'S0N 0J0', 'Town A', 'A Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (4, 2, '2012-07-12', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (5, 2, '2014-01-01','Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (6, 2, '2013-11-09', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (6, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (6, 12);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (3, 'Ten Forward', 'Starship Enterprise', 'S0N 0P0', 'Space', 'Space Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (7, 3, '2013-08-12', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (7, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (8, 3, '2014-10-16', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (8, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (4, 'Monks Cafe', 'Seinfeld Place', 'S0N 0W0', 'New York', 'York Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (9, 4, '2012-11-15', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (10, 4, '2013-01-07', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (10, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (11, 4, '2014-09-09', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 16);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (5, 'Panda Express', 'South Park Ave', 'S0N 0W0', 'South Park', 'SP Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (12, 5, '2011-08-08', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (12, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (13, 5, '2013-06-20', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (13, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (14, 5, '2012-08-23', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (6, 'Insert TV Reference', '9001 place', 'H0H 0H0', 'Caprica City', 'Caprica Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (15, 6, '2014-12-19', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (16, 6, '2014-05-17', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (16, 1);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (17, 6, '2012-12-17', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (7, 'Hogwarts Dining Hall', 'Hogwarts Place', 'S0G 2J0', 'Hogwarts', 'Wizard Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (18, 7, '2014-12-17', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (18, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (19, 7, '2014-04-17', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (19, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (20, 7, '2013-12-17', 'Routine', 'High');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 7);

 --more test information
 
 --a location with very little data, to check the null capabilites of the system
 INSERT INTO location(id, name, rha)
 VALUES (8, '???', 'Questionable Health Authority');
 
 INSERT INTO coordinates (location_id, latitude, longitude)
 VALUES (1, 1.0, 1.0);
 
 INSERT INTO coordinates (location_id, latitude, longitude)
 VALUES (2, 1.0, 1.0);
 
  INSERT INTO coordinates (location_id, latitude, longitude)
 VALUES (5, 26.696545, 2.988281)

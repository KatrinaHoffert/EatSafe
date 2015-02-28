-- EatSafe_test
-- This file will populate the database
-- Before running this file, run CreateTables.sql first
INSERT INTO violation_type (id, description, name,  priority)
VALUES (1, 'Potentially hazardous foods and perishable foods must be stored at 4°C/40°F or below. Hazardous foods must be thawed in a refrigerator or under cold, running water.', 'Refrigeration/Cooling/Thawing (must be 4°C/40°F or lower)', 'Critical Item'),
(2, 'Cook foods to an internal temperature of: a) 63°C (145°F) or above for: eggs (if prepared for immediate service); medium rare beef and veal steaks and roasts; b) 68°C (155°F) or above for: game farm meat products; c) 70°C (158°F) for: fish; d) 71°C (160°F) or above for: ground beef/pork/veal; food made with ground beef/pork/veal, e.g. sausages, meatballs; pork chops, ribs and roasts; e) 74°C (165°F) or above for: ground chicken/turkey; food made with ground chicken/turkey or mixtures containing poultry, meat, fish, or eggs; chicken and turkey breasts, legs, thighs and wings; stuffing (inside a carcass); stuffed pasta; hot dogs; leftovers; egg dishes (if not prepared as specified in 2a); and stuffed fish; f) 85°C (185°C) or above for: chicken and turkey, whole bird. Reheat foods rapidly to an internal temperature of 74°C (165°F) prior to serving. Hot Holding must maintain an internal temperature of 60°C (140°F) or higher.', 'Cooking/Reheating/Hot Holding (must be 60°C/140°F or higher)', 'Critical Item'),
(3, 'Foods must be stored in food grade containers, properly labelled and protected from contamination at all times.', 'Storage/Preparation of Foods', 'Critical Item'),
(4, 'Hand washing must be properly done at appropriate times and intervals. An accessible, plumbed hand basin with hot and cold running water, soap in a dispenser and single-use paper towels in wall-mounted dispensers are required in food preparation areas.Hand washing Procedure: a) Wet hands and exposed arms (at least up to wrist) with warm running water; b) Apply liquid soap; c) Vigorously rub together wet surfaces for at least 20 seconds, lathering at least up to wrist; d) Use a nailbrush under fingernails and other very dirty areas; e) Thoroughly rinse with clean, warm water running from wrists to fingertips; f) Apply soap and lather vigorously again; g) Rinse hands and wrists thoroughly; h) Dry hands with a single-use paper towel; and i) Use paper towel to turn off tap.', 'Hand Washing Facilities/Practices', 'Critical Item'),
(5, 'Good personal hygiene must be practiced at all times. Food handlers with infectious or contagious diseases (or symptoms) should not work. ', 'Food Handler Illness/Hygiene/Habits', 'Critical Item'),
(6, 'Foods must be protected from contamination at all times. ', 'Food Protection', 'Critical Item'),
(7, ' Proper dish washing procedures must be followed. Mechanical washing: dishwashers must be National Sanitation Foundation (NSF) approved or equivalent, designed to wash at 60oC (140oF) and utilize an approved sanitizing agent. Manual washing: (wash/rinse/sanitize in a three-compartment sink): first compartment - clean hot water 44°C (111°F) with detergent; second compartment - clean hot water 44°C (111°F); third compartment - approved sanitizing method.', 'Cleaning/Sanitizing of Equipment/Utensils', 'Critical Item'),
(8, 'Food, water and ice must be from an approved source and must also be wholesome, free from damage or spoilage and transported under proper temperatures, where applicable. ', 'Food Received from Approved Source', 'Critical Item'),
(9, 'Food, water and ice must be from an approved source and must also be wholesome, free from damage or spoilage and transported under proper temperatures, where applicable. ', 'Water Received from Approved Source', 'Critical Item'),
(10, 'Food must be protected from contamination during storage, preparation, display, service and transport. No food is to be stored on the floor unless it is in an approved container. The lowest shelf is to be high enough to allow easy cleaning of the floor.', 'Food Protection', 'General Item'),
(11, 'An accurate, metal-stemmed (food-grade) probe thermometer must be available to monitor temperatures of potentially hazardous foods.', 'Accurate Thermometer Available to Monitor Food Temperatures', 'General Item'),
(12, 'Approved dishwashing facilities must be installed and properly maintained. An adequate supply of cleaning supplies, chemicals, etc. must be available at all times. ''Clean-in-place'' equipment must be washed and sanitized according to manufacturers instructions.', 'Construction/Storage/Cleaning of Equipment/Utensils', 'General Item'),
(13, 'An adequate number of approved, covered garbage containers must be provided at all food preparation areas. Containers are to be kept clean and the contents removed at least daily. Garbage storage must be of an approved design with a lid that seals. It must be kept clean and free of vermin and serviced as required. ', 'Garbage Storage and/or Removal', 'General Item'),
(14, 'All restaurants are to be free of vermin.', 'Insect/Rodent Control', 'General Item'),
(15, 'Floors, walls and ceilings of all rooms in which food is stored, prepared or served or in which dishes, utensils and equipment are washed or stored should be kept clean and in good repair.', 'Construction/Maintenance and/or Cleaning of Premises', 'General Item'),
(16, 'Approved plumbing must be installed and properly maintained to prevent food contamination.  Light shields or shatterproof bulbs are to be provided in every room in which food is prepared or stored. Unless otherwise approved, every restaurant is to have a ventilation system that prevents the accumulation of odours, smoke, grease/oils and condensation.', 'Plumbing/Lighting/Ventilation', 'General Item');
--7 total locations
--20 total inspections
--20 total violations

--Location 1
/*
Location Summary
3 inspections
0 violations
*/
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (1, 'Andrews Kitchen', 'My Place', 'S0N 0H0', 'Town A', 'A Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (1, 1, 'Fri Nov 07 00:00:00 GMT-06:00 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (2, 1, 'Mon Nov 18 00:00:00 GMT-06:00 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (3, 1, 'Mon Dec 03 00:00:00 GMT-06:00 2012', 'Routine', 'Low');

 -- Location 2
 /*
 Location Summary
 3 inspections
 5 violations
 */
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (2, 'Andrews Bathroom', 'My Place', 'S0N 0J0', 'Town A', 'A Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (4, 2, 'Mon Nov 10 00:00:00 GMT-06:00 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (5, 2, 'Fri Aug 29 00:00:00 GMT-06:00 2014', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (6, 2, 'Mon May 12 00:00:00 GMT-06:00 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (6, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (6, 12);

 --Location 3
  /*
 Location Summary
 2 inspections
 2 violations
 */
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (3, 'Ten Forward', 'Starship Enterprise', 'S0N 0P0', 'Space', 'Space Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (7, 3, 'Tue Sep 23 00:00:00 GMT-06:00 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (7, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (8, 3, 'Fri May 02 00:00:00 GMT-06:00 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (8, 15);

 --Location 4
  /*
 Location Summary
 3 inspections
 3 violations
 */
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (4, 'Monks Cafe', 'Seinfeld Place', 'S0N 0W0', 'New York', 'York Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (9, 4, 'Thu Oct 09 00:00:00 GMT-06:00 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (10, 4, 'Wed Oct 23 00:00:00 GMT-06:00 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (10, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (11, 4, 'Fri Sep 07 00:00:00 GMT-06:00 2012', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 16);

 --location 5
  /*
 Location Summary
 3 inspections
 4 violations
 */
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (5, 'Panda Express', 'South Park Ave', 'S0N 0W0', 'South Park', 'SP Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (12, 5, 'Wed Oct 23 00:00:00 GMT-06:00 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (12, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (13, 5, 'Thu Oct 25 00:00:00 GMT-06:00 2012', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (13, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (14, 5, 'Mon Jan 16 00:00:00 GMT-06:00 2012', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 15);

 --Location 6
 /*
 Location Summary
 3 inspections
 1 violations
 */
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (6, 'Insert TV Reference', '9001 place', 'H0H 0H0', 'Caprica City', 'Caprica Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (15, 6, 'Wed Jul 16 00:00:00 GMT-06:00 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (16, 6, 'Tue Jun 25 00:00:00 GMT-06:00 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (16, 1);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (17, 6, 'Thu Jun 28 00:00:00 GMT-06:00 2012', 'Routine', 'Low');

 --Location 7
  /*
 Location Summary
 3 inspections
 5 violations
 */
INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (7, 'Hogwarts Dining Hall', 'Hogwarts Place', 'S0G 2J0', 'Hogwarts', 'Wizard Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (18, 7, 'Wed Dec 17 00:00:00 GMT-06:00 2014', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (18, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (19, 7, 'Fri Oct 24 00:00:00 GMT-06:00 2014', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (19, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (20, 7, 'Thu Sep 25 00:00:00 GMT-06:00 2014', 'Routine', 'High');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 7);


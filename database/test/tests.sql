-- EatSafe
-- This file will populate the database
-- Before running this file, run CreateTables.sql first
INSERT INTO violation_type (id, description, name,  priority)
VALUES (1, 'Potentially hazardous foods and perishable foods must be stored at 4°C/40°F or below. Hazardous foods must be thawed in a refrigerator or under cold, running water.', 'Refrigeration/Cooling/Thawing (must be 4°C/40°F or lower)', 'Critical Item'),
(2, 'Cook foods to an internal temperature of: a) 63°C (145°F) or above for: eggs (if prepared for immediate service) medium rare beef and veal steaks and roasts b) 68°C (155°F) or above for: game farm meat products c) 70°C (158°F) for: fish d) 71°C (160°F) or above for: ground beef/pork/veal, food made with ground beef/pork/veal, e.g. sausages, meatballs, pork chops, ribs and roasts. e) 74°C (165°F) or above for: ground chicken/turkey, food made with ground chicken/turkey or mixtures containing poultry, meat, fish, or eggs. chicken and turkey breasts, legs, thighs and wings. stuffing (inside a carcass)  stuffed pasta  hot dogs  leftovers  egg dishes (if not prepared as specified in 2a)  and stuffed fish  f) 85°C (185°C) or above for: chicken and turkey, whole bird. Reheat foods rapidly to an internal temperature of 74°C (165°F) prior to serving. Hot Holding must maintain an internal temperature of 60°C (140°F) or higher.', 'Cooking/Reheating/Hot Holding (must be 60°C/140°F or higher)', 'Critical Item'),
(3, 'Foods must be stored in food grade containers, properly labelled and protected from contamination at all times.', 'Storage/Preparation of Foods', 'Critical Item'),
(4, 'Hand washing must be properly done at appropriate times and intervals. An accessible, plumbed hand basin with hot and cold running water, soap in a dispenser and single-use paper towels in wall-mounted dispensers are required in food preparation areas.Hand washing Procedure: a) Wet hands and exposed arms (at least up to wrist) with warm running water  b) Apply liquid soap  c) Vigorously rub together wet surfaces for at least 20 seconds, lathering at least up to wrist  d) Use a nailbrush under fingernails and other very dirty areas  e) Thoroughly rinse with clean, warm water running from wrists to fingertips  f) Apply soap and lather vigorously again  g) Rinse hands and wrists thoroughly  h) Dry hands with a single-use paper towel  and i) Use paper towel to turn off tap.', 'Hand Washing Facilities/Practices', 'Critical Item'),
(5, 'Good personal hygiene must be practiced at all times. Food handlers with infectious or contagious diseases (or symptoms) should not work. ', 'Food Handler Illness/Hygiene/Habits', 'Critical Item'),
(6, 'Foods must be protected from contamination at all times. ', 'Food Protection', 'Critical Item'),
(7, ' Proper dish washing procedures must be followed. Mechanical washing: dishwashers must be National Sanitation Foundation (NSF) approved or equivalent, designed to wash at 60oC (140oF) and utilize an approved sanitizing agent. Manual washing: (wash/rinse/sanitize in a three-compartment sink): first compartment - clean hot water 44°C (111°F) with detergent  second compartment - clean hot water 44°C (111°F)  third compartment - approved sanitizing method.', 'Cleaning/Sanitizing of Equipment/Utensils', 'Critical Item'),
(8, 'Food, water and ice must be from an approved source and must also be wholesome, free from damage or spoilage and transported under proper temperatures, where applicable. ', 'Food Received from Approved Source', 'Critical Item'),
(9, 'Food, water and ice must be from an approved source and must also be wholesome, free from damage or spoilage and transported under proper temperatures, where applicable. ', 'Water Received from Approved Source', 'Critical Item'),
(10, 'Food must be protected from contamination during storage, preparation, display, service and transport. No food is to be stored on the floor unless it is in an approved container. The lowest shelf is to be high enough to allow easy cleaning of the floor.', 'Food Protection', 'General Item'),
(11, 'An accurate, metal-stemmed (food-grade) probe thermometer must be available to monitor temperatures of potentially hazardous foods.', 'Accurate Thermometer Available to Monitor Food Temperatures', 'General Item'),
(12, 'Approved dishwashing facilities must be installed and properly maintained. An adequate supply of cleaning supplies, chemicals, etc. must be available at all times. ''Clean-in-place'' equipment must be washed and sanitized according to manufacturers instructions.', 'Construction/Storage/Cleaning of Equipment/Utensils', 'General Item'),
(13, 'An adequate number of approved, covered garbage containers must be provided at all food preparation areas. Containers are to be kept clean and the contents removed at least daily. Garbage storage must be of an approved design with a lid that seals. It must be kept clean and free of vermin and serviced as required. ', 'Garbage Storage and/or Removal', 'General Item'),
(14, 'All restaurants are to be free of vermin.', 'Insect/Rodent Control', 'General Item'),
(15, 'Floors, walls and ceilings of all rooms in which food is stored, prepared or served or in which dishes, utensils and equipment are washed or stored should be kept clean and in good repair.', 'Construction/Maintenance and/or Cleaning of Premises', 'General Item'),
(16, 'Approved plumbing must be installed and properly maintained to prevent food contamination.  Light shields or shatterproof bulbs are to be provided in every room in which food is prepared or stored. Unless otherwise approved, every restaurant is to have a ventilation system that prevents the accumulation of odours, smoke, grease/oils and condensation.', 'Plumbing/Lighting/Ventilation', 'General Item'); 


INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (1, 'Burstall Arena - Kitchen', '615 Maharg Ave', 'S0N 0H0', 'Burstall', 'Cypress Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (1, 1, '2014-11-07', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (1, 12);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (1, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (2, 1, '2013-11-18', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (2, 12);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (2, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (3, 1, '2012-12-03', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (3, 15a);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (2, 'Cabri Inn - Kitchen', '103 Railway Ave S', 'S0N 0J0', 'Cabri', 'Cypress Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (4, 2, '2014-11-10', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (5, 2, '2014-08-29', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (6, 2, '2014-05-12', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (6, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (6, 12);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (3, 'Consul Saloon', '120 Railway Ave', 'S0N 0P0', 'Consul', 'Cypress Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (7, 3, '2014-09-23', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (7, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (8, 3, '2014-05-02', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (8, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (4, 'Frontier Motel - Restaurant', 'Hwy 18 E', 'S0N 0W0', 'Frontier', 'Cypress Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (9, 4, '2014-10-09', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (10, 4, '2013-10-23', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (10, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (11, 4, '2012-09-07', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 16);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (5, 'Frontier Recreation Centre - Concession', '306 1st St E', 'S0N 0W0', 'Frontier', 'Cypress Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (12, 5, '2013-10-23', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (12, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (13, 5, '2012-10-25', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (13, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (14, 5, '2012-01-16', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (6, 'Lorraine''s Kitchen', 'North Qu''appelle', 'DATA MISSING', 'DATA MISSING', 'Regina QuAppelle Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (15, 6, '2014-07-16', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (16, 6, '2013-06-25', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (16, 1);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (17, 6, '2012-06-28', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (7, 'Imperial Family Restaurant', '214 Royal St.', 'S0G 2J0', 'Imperial', 'Regina QuAppelle Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (18, 7, '2014-12-17', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (18, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (19, 7, '2014-10-24', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (19, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (20, 7, '2014-09-25', 'Routine', 'High');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 7);


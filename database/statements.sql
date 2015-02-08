-- EatSafe
-- This file will populate the database
-- Before run this file, run CreateTables.sql first



-- First populate the violation_type table


INSERT INTO violation_type (id, description, name,  priority)
VALUES (1, 'Potentially hazardous foods and perishable foods must be stored at 4oC/40oF or below. Hazardous foods must be thawed in a refrigerator or under cold, running water.', 'Refrigeration/Cooling/Thawing (must be 4°C/40°F or lower)', 'Critical Item'),
(2, 'Cook foods to an internal temperature of: a) 63oC (145oF) or above for: eggs (if prepared for immediate service); medium rare beef and veal steaks and roasts; b) 68oC (155oF) or above for: game farm meat products; c) 70oC (158oF) for: fish; d) 71oC (160oF) or above for: ground beef/pork/veal; food made with ground beef/pork/veal, e.g. sausages, meatballs; pork chops, ribs and roasts; e) 74oC (165oF) or above for: ground chicken/turkey; food made with ground chicken/turkey or mixtures containing poultry, meat, fish, or eggs; chicken and turkey breasts, legs, thighs and wings; stuffing (inside a carcass); stuffed pasta; hot dogs; leftovers; egg dishes (if not prepared as specified in 2a); and stuffed fish; f) 85oC (185oC) or above for: chicken and turkey, whole bird. Reheat foods rapidly to an internal temperature of 74oC (165oF) prior to serving. Hot Holding must maintain an internal temperature of 60oC (140oF) or higher.', 'Cooking/Reheating/Hot Holding (must be 60°C/140°F or higher)', 'Critical Item'),
(3, 'Foods must be stored in food grade containers, properly labelled and protected from contamination at all times.', 'Storage/Preparation of Foods', 'Critical Item'),
(4, 'Hand washing must be properly done at appropriate times and intervals. An accessible, plumbed hand basin with hot and cold running water, soap in a dispenser and single-use paper towels in wall-mounted dispensers are required in food preparation areas.Hand washing Procedure: a) Wet hands and exposed arms (at least up to wrist) with warm running water; b) Apply liquid soap; c) Vigorously rub together wet surfaces for at least 20 seconds, lathering at least up to wrist; d) Use a nailbrush under fingernails and other very dirty areas; e) Thoroughly rinse with clean, warm water running from wrists to fingertips; f) Apply soap and lather vigorously again; g) Rinse hands and wrists thoroughly; h) Dry hands with a single-use paper towel; and i) Use paper towel to turn off tap.', 'Hand Washing Facilities/Practices', 'Critical Item'),
(5, 'Good personal hygiene must be practiced at all times. Food handlers with infectious or contagious diseases (or symptoms) should not work. ', 'Food Handler Illness/Hygiene/Habits', 'Critical Item'),
(7, ' Proper dish washing procedures must be followed. Mechanical washing: dishwashers must be National Sanitation Foundation (NSF) approved or equivalent, designed to wash at 60oC (140oF) and utilize an approved sanitizing agent. Manual washing: (wash/rinse/sanitize in a three-compartment sink): first compartment - clean hot water 44oC (111oF) with detergent; second compartment - clean hot water 44oC (111oF); third compartment - approved sanitizing method.', 'Cleaning/Sanitizing of Equipment/Utensils', 'Critical Item'),
(10, 'Food must be protected from contamination during storage, preparation, display, service and transport. No food is to be stored on the floor unless it is in an approved container. The lowest shelf is to be high enough to allow easy cleaning of the floor.', 'Food Protection', 'General Item'),
(11, 'An accurate, metal-stemmed (food-grade) probe thermometer must be available to monitor temperatures of potentially hazardous foods.', 'Accurate Thermometer Available to Monitor Food Temperatures', 'General Item'),
(12, 'Approved dishwashing facilities must be installed and properly maintained. An adequate supply of cleaning supplies, chemicals, etc. must be available at all times. ''Clean-in-place'' equipment must be washed and sanitized according to manufacturers instructions.', 'Construction/Storage/Cleaning of Equipment/Utensils', 'General Item'),
(14, 'All restaurants are to be free of vermin.', 'Insect/Rodent Control', 'General Item'),
(15, 'Floors, walls and ceilings of all rooms in which food is stored, prepared or served or in which dishes, utensils and equipment are washed or stored should be kept clean and in good repair.', 'Construction/Maintenance and/or Cleaning of Premises', 'General Item'),
(16, 'Approved plumbing must be installed and properly maintained to prevent food contamination.  Light shields or shatterproof bulbs are to be provided in every room in which food is prepared or stored. Unless otherwise approved, every restaurant is to have a ventilation system that prevents the accumulation of odours, smoke, grease/oils and condensation.', 'Plumbing/Lighting/Ventilation', 'General Item');


INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (1, '24 And 7 Convenience Store', '101 - 412 Willowgrove Square', 'S7W 0B1', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (1, 1, 'Mon Aug 25 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (1, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (1, 16);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (2, '7 - Eleven Central', '1100 A Central AVE', 'S7N 2H1', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (2, 2, 'Thu Dec 11 00:00:00 CST 2014', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (3, 2, 'Thu Nov 20 00:00:00 CST 2014', 'Routine', 'High');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (3, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (3, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (4, 2, 'Thu Nov 28 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (4, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (3, '7 Eleven - 2nd Ave. North', '380 2nd AVE N', 'S7K 2B9', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (5, 3, 'Thu Jun 26 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (6, 3, 'Thu Jun 27 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (7, 3, 'Thu Jun 28 00:00:00 CST 2012', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (4, '7 Eleven - Idylwyld', '1435 Idylwyld DR N', 'S7L 1A5', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (8, 4, 'Wed Sep 10 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (8, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (9, 4, 'Tue Sep 24 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (9, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (10, 4, 'Fri Sep 14 00:00:00 CST 2012', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (5, '7 Eleven', '1001 8th ST E', 'S7H 0S2', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (11, 5, 'Mon Nov 10 00:00:00 CST 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (11, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (12, 5, 'Fri Dec 27 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (13, 5, 'Fri Dec 28 00:00:00 CST 2012', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (6, '7 Eleven', '8 Assiniboine DR', 'S7K 1H2', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (14, 6, 'Wed Apr 09 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 7);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (14, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (15, 6, 'Thu Apr 25 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (16, 6, 'Tue May 15 00:00:00 CST 2012', 'Follow-up', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (16, 11);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (7, '7 Eleven', '835 A Broadway AVE', 'S7N 1B5', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (17, 7, 'Mon Jan 20 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (17, 10);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (18, 7, 'Wed Jan 09 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (18, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (18, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (19, 7, 'Mon Feb 06 00:00:00 CST 2012', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (19, 16);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (8, '7 Eleven Food Store  #29930', '1015 McKercher DR', 'S7H 5P1', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (20, 8, 'Tue Jun 03 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (20, 3);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (21, 8, 'Fri Jan 17 00:00:00 CST 2014', 'Special', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (21, 3);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (21, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (21, 7);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (21, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (21, 16);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (22, 8, 'Fri Aug 23 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (22, 3);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (22, 15);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (22, 16);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (9, '7 Eleven Food Store - 8th St.', '1930 8th ST E', 'S7H 0T7', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (23, 9, 'Tue Apr 01 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (23, 2);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (23, 3);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (24, 9, 'Tue May 14 00:00:00 CST 2013', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (25, 9, 'Tue May 07 00:00:00 CST 2013', 'Routine', 'High');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (25, 7);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (10, '7 Eleven Food Store', '3303 A 33rd ST W', 'S7L 4P5', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (26, 10, 'Fri Jan 09 00:00:00 CST 2015', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (27, 10, 'Mon Apr 07 00:00:00 CST 2014', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (28, 10, 'Fri Jan 10 00:00:00 CST 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (28, 7);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (28, 12);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (28, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (11, '7-11 Food Store', '234 Pendygrasse RD', 'S7M 5A4', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (29, 11, 'Tue Oct 07 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (30, 11, 'Fri Nov 01 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (31, 11, 'Mon Oct 22 00:00:00 CST 2012', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (12, '2nd Avenue Grill', '10 - 123 2nd AVE S', 'S7H 0V9', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (32, 12, 'Fri Dec 05 00:00:00 CST 2014', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (33, 12, 'Fri Sep 26 00:00:00 CST 2014', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (33, 7);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (34, 12, 'Mon Jul 07 00:00:00 CST 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (34, 1);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (13, '786 Shawarma', 'Unit 10B 234 Primrose Dr', 'S7M 6T6', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (35, 13, 'Fri Sep 12 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (35, 3);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (35, 14);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (36, 13, 'Wed Oct 09 00:00:00 CST 2013', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (37, 13, 'Tue Oct 01 00:00:00 CST 2013', 'Follow-up', 'High');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (37, 2);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (37, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (37, 7);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (37, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (37, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (14, '8th Street China Supermarket', '1024 Louise Avenue', 'S7H 2P6', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (38, 14, 'Mon Sep 08 00:00:00 CST 2014', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (39, 14, 'Tue Jun 10 00:00:00 CST 2014', 'Follow-up', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (39, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (39, 5);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (40, 14, 'Tue Mar 18 00:00:00 CST 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (40, 5);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (40, 7);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (40, 16);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (15, '2x   City Hospital Kitchen', '701 Queen ST', 'S7K 0M7', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (41, 15, 'Wed Aug 20 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (42, 15, 'Tue Feb 25 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (43, 15, 'Fri Sep 06 00:00:00 CST 2013', 'Follow-up', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (16, '2x   Ruh - Food & Nutrition Services', '103 Hospital DR', 'S7N 0W8', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (44, 16, 'Tue Dec 16 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (44, 14);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (44, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (45, 16, 'Tue Jul 22 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (46, 16, 'Thu Aug 29 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (46, 15);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (17, '2x   St. Paul''s Hospital Kitchen', '1702 20th ST W', 'S7M 0Z9', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (47, 17, 'Mon Mar 17 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (48, 17, 'Fri Sep 27 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (49, 17, 'Mon Mar 11 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (18, '2x St. Volodymyr Terrace', '3102 Louise PL', 'S7J 4X4', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (50, 18, 'Thu Apr 24 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (50, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (51, 18, 'Thu Nov 28 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (51, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (52, 18, 'Tue May 14 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (19, '33rd Street Cafe', '1624 33rd ST W', 'S7L 0X3', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (53, 19, 'Mon Jun 16 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (54, 19, 'Thu Jun 27 00:00:00 CST 2013', 'Routine', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (55, 19, 'Wed Jun 27 00:00:00 CST 2012', 'Follow-up', 'Low');

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (20, '3d Pizza', 'E-511 33rd St West', 'S7L 0V7', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (56, 20, 'Mon Aug 11 00:00:00 CST 2014', 'Follow-up', 'Low');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (57, 20, 'Fri May 02 00:00:00 CST 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (57, 5);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (57, 7);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (21, '7 - 11 22nd Street West', '1528 22nd ST W', 'S7M 0T1', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (58, 21, 'Mon Sep 29 00:00:00 CST 2014', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (58, 1);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (58, 4);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (58, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (58, 15);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (59, 21, 'Thu Apr 17 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (59, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (59, 12);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (60, 21, 'Wed Nov 27 00:00:00 CST 2013', 'Routine', 'Moderate');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (60, 2);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (60, 10);

INSERT INTO violation(inspection_id, violation_id)
 VALUES (60, 12);

INSERT INTO location(id, name, address, postcode, city, rha)
 VALUES (22, '1 Stop Convenience & Dollar Store', '1631 29th Street W', 'S7L 0N6', 'Saskatoon', 'Saskatoon Health Authority');

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (61, 22, 'Fri Oct 24 00:00:00 CST 2014', 'Routine', 'Low');

INSERT INTO violation(inspection_id, violation_id)
 VALUES (61, 7);

INSERT INTO inspection(id, location_id, inspection_date, inspection_type, reinspection_priority)
 VALUES (62, 22, 'Tue Nov 26 00:00:00 CST 2013', 'Routine', 'Low');


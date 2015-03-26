COPY violation_type (id, description, name, priority) FROM STDIN;
1	parser.desc.1	parser.name.1	parser.priority.critical
2	parser.desc.2	parser.name.2	parser.priority.critical
3	parser.desc.3	parser.name.3	parser.priority.critical
4	parser.desc.4	parser.name.4	parser.priority.critical
5	parser.desc.5	parser.name.5	parser.priority.critical
6	parser.desc.6	parser.name.6	parser.priority.critical
7	parser.desc.7	parser.name.7	parser.priority.critical
8	parser.desc.8	parser.name.8	parser.priority.critical
9	parser.desc.9	parser.name.9	parser.priority.critical
10	parser.desc.10	parser.name.10	parser.priority.general
11	parser.desc.11	parser.name.11	parser.priority.general
12	parser.desc.12	parser.name.12	parser.priority.general
13	parser.desc.13	parser.name.13	parser.priority.general
14	parser.desc.14	parser.name.14	parser.priority.general
15	parser.desc.15	parser.name.15	parser.priority.general
16	parser.desc.16	parser.name.16	parser.priority.general
\.

COPY location (id, name, address, postcode, city, rha) FROM STDIN;
1	Tasty J's	305 Pacific Avenue	S0L 2A0	Luseland	Heartland Health Authority
2	Water Front Lodge	\N	\N	\N	Northern Health - Mamaw/Keewa/Athab
3	Robinson Country Cookhouse & Saloon	Cupar	\N	Cupar	Regina QuAppelle Health Authority
\.

COPY inspection (id, location_id, inspection_date, inspection_type, reinspection_priority) FROM STDIN;
1	1	2014-12-05	Routine	Moderate
2	1	2014-02-18	Routine	Low
3	1	2013-10-24	Routine	Moderate
4	2	2014-09-23	Routine	Low
5	2	2013-08-01	Routine	Low
6	2	2012-02-08	Routine	Low
7	3	2014-02-14	Routine	Low
8	3	2013-02-25	Follow-up	Low
9	3	2012-08-08	Routine	Moderate
\.

COPY violation (inspection_id, violation_id) FROM STDIN;
1	4
1	10
1	15
2	10
3	10
3	12
3	14
4	4
5	11
6	1
6	12
7	10
7	12
7	15
8	4
9	4
9	11
\.

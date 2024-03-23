-- CONFERENCE_ROOM >>> data populate
INSERT INTO CONFERENCE_ROOM(NAME, CODE, CAPACITY, SLOT_INTERVAL, WORKING_HOUR_WINDOW, CREATED_BY, CREATED_DATE) VALUES ('Amaze', '501', 3, 15, '9:00-18:00', 'system', CURRENT_TIMESTAMP);
INSERT INTO CONFERENCE_ROOM(NAME, CODE, CAPACITY, SLOT_INTERVAL, WORKING_HOUR_WINDOW, CREATED_BY, CREATED_DATE) VALUES ('Beauty', '502', 7, 15, '9:00-18:00', 'system', CURRENT_TIMESTAMP);
INSERT INTO CONFERENCE_ROOM(NAME, CODE, CAPACITY, SLOT_INTERVAL, WORKING_HOUR_WINDOW, CREATED_BY, CREATED_DATE) VALUES ('Inspire', '503', 12, 15, '9:00-18:00', 'system', CURRENT_TIMESTAMP);
INSERT INTO CONFERENCE_ROOM(NAME, CODE, CAPACITY, SLOT_INTERVAL, WORKING_HOUR_WINDOW, CREATED_BY, CREATED_DATE) VALUES ('Strive', '504', 20, 15, '9:00-18:00', 'system', CURRENT_TIMESTAMP);

-- MAINTENANCE_SETTINGS >>> data populate
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Amaze'),'9:00-9:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Amaze'),'13:00-13:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Amaze'),'17:00-17:15', 'system', CURRENT_TIMESTAMP);

INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Beauty'),'9:00-9:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Beauty'),'13:00-13:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Beauty'),'17:00-17:15', 'system', CURRENT_TIMESTAMP);

INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Inspire'),'9:00-9:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Inspire'),'13:00-13:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Inspire'),'17:00-17:15', 'system', CURRENT_TIMESTAMP);

INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Strive'),'9:00-9:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Strive'),'13:00-13:15', 'system', CURRENT_TIMESTAMP);
INSERT INTO MAINTENANCE_SETTINGS(CONFERENCE_ROOM_ID, MAINTENANCE_SLOT, CREATED_BY, CREATED_DATE) VALUES ((SELECT ID FROM CONFERENCE_ROOM WHERE NAME = 'Strive'),'17:00-17:15', 'system', CURRENT_TIMESTAMP);
-- Sample seed data for Police Force Dogs application
-- Inserts 1 police_force, 3 kennels, 3 kennel_characteristics,
-- 6 dog_status entries and 6 dog_leaving_reasons.

-- Police force
INSERT INTO police_force (id, name, code, created_at) VALUES
(1, 'Somerset Police', 'SOM', CURRENT_TIMESTAMP);

-- Kennel characteristics
INSERT INTO kennel_characteristics (id, name, created_at) VALUES
(1, 'High energy', CURRENT_TIMESTAMP),
(2, 'Low tolerance for other dogs', CURRENT_TIMESTAMP),
(3, 'Escape risk', CURRENT_TIMESTAMP);

-- Dog status values - as per specification
INSERT INTO dog_status (id, name, code, description, created_at) VALUES
(1, 'In Training', 'IN_TRAINING', 'Dog is currently in training', CURRENT_TIMESTAMP),
(2, 'In Service', 'IN_SERVICE', 'Dog is actively deployed on duty', CURRENT_TIMESTAMP),
(3, 'Retired', 'RETIRED', 'Dog has been retired from service', CURRENT_TIMESTAMP),
(4, 'Left', 'LEFT', 'Dog has left the organisation (other)', CURRENT_TIMESTAMP);

-- Dog leaving reasons - as per specification
INSERT INTO dog_leaving_reasons (id, name, code, description, created_at) VALUES
(1, 'Transferred', 'TRANSFERRED', 'Transferred to another unit/force', CURRENT_TIMESTAMP),
(2, 'Retired (Put Down)', 'RETIRED_PUT_DOWN', 'Retired and put down', CURRENT_TIMESTAMP),
(3, 'KIA', 'KIA', 'Killed in action', CURRENT_TIMESTAMP),
(4, 'Rejected', 'REJECTED', 'Rejected (failed selection/training)', CURRENT_TIMESTAMP),
(5, 'Retired (Re-housed)', 'RETIRED_REHOUSED', 'Retired and re-housed with a civilian', CURRENT_TIMESTAMP),
(6, 'Died', 'DIED', 'Died of natural causes or illness', CURRENT_TIMESTAMP);

-- Kennels
INSERT INTO kennel (id, name, location, created_at) VALUES
(1, 'North Kennels', 'North District - Unit A', CURRENT_TIMESTAMP),
(2, 'South Kennels', 'South District - Unit B', CURRENT_TIMESTAMP),
(3, 'Central Kennels', 'Central Compound', CURRENT_TIMESTAMP);

-- Sample dog: comes from North Kennels (kennel id = 1), is In Service (status id = 2), and has two kennel characteristics (ids 1 and 2)
INSERT INTO dogs (id, name, breed, supplier, badge_id, gender, birth_date, date_acquired, current_status_id, leaving_date, leaving_reason_id, kennel_id, police_force_id, deleted, created_at) VALUES
(1, 'Ragnar', 'Belgian Malinois', 'North Kennels', 'B-001', 'Male', '2018-04-02', '2018-10-10', 2, NULL, NULL, 1, 1, FALSE, CURRENT_TIMESTAMP);

-- Link the dog to two kennel characteristics via the join table
INSERT INTO dog_kennel_characteristics (dog_id, kennel_characteristic_id) VALUES
(1, 1),
(1, 2);

-- End of seed data



--
-- Structure for table mydashboard_grusupply_demand
--

DROP TABLE IF EXISTS mydashboard_grusupply_demand;
CREATE TABLE mydashboard_grusupply_demand (
demand_id int NOT NULL,
is_read smallint(1) default '0' NOT NULL,
PRIMARY KEY (demand_id)
);

--
-- Structure for table mydashboard_grusupply_tag_demand_type
--

DROP TABLE IF EXISTS mydashboard_grusupply_tag_demand_type;
CREATE TABLE mydashboard_grusupply_tag_demand_type (
id int AUTO_INCREMENT,
id_demand_type int NOT NULL,
tag varchar(255) NOT NULL,
PRIMARY KEY (id)
);
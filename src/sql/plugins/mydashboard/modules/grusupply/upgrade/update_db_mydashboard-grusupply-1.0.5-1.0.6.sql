--
-- The table mydashboard_grusupply_tag_demand_type is not used anymore:
-- DemandType now stores the tags in the metadata field
-- /!\ WARNING /!\ EXECUTE THE SCRIPT IN THE "migration" FOLDER FIRST TO NOT LOSE THE EXISTING TAGS
--

DROP TABLE IF EXISTS mydashboard_grusupply_tag_demand_type;

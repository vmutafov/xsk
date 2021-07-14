CREATE USER XS_MIGRATOR PASSWORD Toor1234 NO FORCE_FIRST_PASSWORD_CHANGE;

CALL grant_activated_role('sap.hana.xs.lm.roles::Administrator' , 'XS_MIGRATOR');

GRANT SELECT ON "_SYS_REPO"."ACTIVE_OBJECT_TEXT" TO "XS_MIGRATOR";
GRANT SELECT ON "_SYS_REPO"."ACTIVE_OBJECT_TEXT_CONTENT" TO "XS_MIGRATOR";
GRANT SELECT ON "_SYS_REPO"."ACTIVE_CONTENT_TEXT" TO "XS_MIGRATOR";
GRANT SELECT ON "_SYS_REPO"."ACTIVE_CONTENT_TEXT_CONTENT" TO "XS_MIGRATOR";
GRANT SELECT ON "_SYS_BI"."M_SCHEMA_MAPPING" TO "XS_MIGRATOR";

GRANT EXECUTE ON "SYS"."GET_OBJECT_DEFINITION" TO "XS_MIGRATOR";
GRANT EXECUTE ON "SYS"."GET_OBJECTS_IN_DDL_STATEMENT" TO "XS_MIGRATOR";

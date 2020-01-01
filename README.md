# dingtalk-springboot
Dingtalk enterprise application back-end (Springboot)

trigger
```sql
CREATE DEFINER=`root`@`localhost` TRIGGER `update_dc_summary` AFTER INSERT ON `dc_record` FOR EACH ROW BEGIN
	DECLARE dc_sum DOUBLE;
	SELECT COUNT(dc) INTO dc_sum FROM dc_record WHERE applicant_id = new.applicant_id AND yearmonth = new.yearmonth and `week` = new.`week` LIMIT 1;
	
	if EXISTS(SELECT id FROM dc_summary WHERE user_id = new.applicant_id and yearmonth = new.yearmonth and `week` = new.`week` LIMIT 1) THEN
		UPDATE dc_summary SET dc = dc_sum WHERE user_id = new.applicant_id and yearmonth = new.yearmonth and `week` = new.`week`;
	ELSE
		INSERT INTO dc_summary(user_id, yearmonth, `week`, dc) VALUES (new.applicant_id, new.yearmonth, new.`week`, new.dc);
	END if;
	

END;
```

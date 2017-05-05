DELIMITER !!
CREATE PROCEDURE `seckill`.`execute_seckill`
  (in v_seckill_id BIGINT, in v_user_phone BIGINT, in v_create_time TIMESTAMP, out r_result INT )
BEGIN
  DECLARE v_count INT;
  INSERT ignore INTO success_killed(seckill_id, user_phone, create_time)
    VALUES(v_seckill_id, v_user_phone, v_create_time);
  SELECT ROW_COUNT() INTO v_count;
  IF v_count < 0 THEN
    ROLLBACK;
    SET r_result = -2;
  ELSEIF v_count = 0 THEN
    ROLLBACK;
    SET r_result = -1;
  ELSE
    UPDATE seckill
    SET number = number - 1
    WHERE seckill_id = v_seckill_id
    AND v_create_time > start_time
    AND v_create_time < end_time;
    SELECT ROW_COUNT() INTO v_count;
    IF v_count < 0 THEN
      ROLLBACK;
      SET r_result = -2;
    ELSEIF v_count = 0 THEN
      ROLLBACK;
      SET r_result = -1;
    ELSE
      COMMIT;
      SET r_result = 1;
    END IF;
  END IF;
END

DELIMITER ;

SET @r_result = -3;
CALL execute_seckill(1006, 74185296314, now(), @r_result);
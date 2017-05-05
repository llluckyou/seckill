-- 创建秒杀数据库
CREATE DATABASE seckill;
-- 使用数据库
USE seckill;

CREATE TABLE seckill(
  seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '秒杀id',
  name VARCHAR(120) NOT NULL COMMENT '商品名称',
  number int NOT NULL COMMENT '库存数量',
--  'price' int NOT NULL COMMENT '商品价格',
  start_time TIMESTAMP NOT NULL COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL COMMENT '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀数据表';

CREATE TABLE seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
name varchar(120) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '库存数量',
start_time timestamp NOT NULL COMMENT '秒杀开启时间',
end_time timestamp NOT NULL COMMENT '秒杀结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--插入数据

INSERT INTO seckill(name,number,start_time,end_time)
VALUES
  ('200秒杀菲尔可忍者二代', 100, '2017-05-02 00:00:00', '2017-05-03 00:00:00'),
  ('200秒杀iphone7', 100, '2017-05-02 00:00:00', '2017-05-03 00:00:00'),
  ('200秒杀MacBook', 100, '2017-05-02 00:00:00', '2017-05-03 00:00:00'),
  ('200秒IPad', 100, '2017-05-02 00:00:00', '2017-05-03 00:00:00');

--秒杀明细表

CREATE TABLE success_killed (
  'seckill_id' bigint NOT NULL COMMENT '秒杀id',
  'user_phone' bigint NOT NULL COMMENT '用户手机号',
  'state' tinyint NOT NULL COMMENT '状态:-1 : 无效  0 : 成功 1 : 已付款',
  'create_time' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀成功时间',

  PRIMARY KEY (seckill_id, user_phone), -- 联合主键，每个用户对单个秒杀只能成功一次，可以用于过滤
  key idx_create_time (create_time),
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';


CREATE TABLE `oauth_client_details`(
`id` int(11) NOT NULL AUTO_INCREMENT,
`client_id` VARCHAR(255) NOT NULL,
`client_secret` VARCHAR(255) NOT NULL,
`resource_ids` VARCHAR(255) DEFAULT NULL,
`scope` VARCHAR(1000) DEFAULT NULL,
`authorized_grant_types` VARCHAR(255) DEFAULT NULL,
`web_server_redirect_uri` VARCHAR(255) DEFAULT NULL,
`authorities` VARCHAR(255) DEFAULT NULL,
`access_token_validity` INT(11) DEFAULT NULL,
`refresh_token_validity` INT(11) DEFAULT NULL,
`additional_information` VARCHAR(4096) DEFAULT NULL,
`autoapprove` VARCHAR(255) DEFAULT NULL,
`insert_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
PRIMARY KEY (`id`),
UNIQUE KEY `id_UNIQUE` (`id`),
UNIQUE KEY `client_id_UNIQUE` (`client_id`));
 
 INSERT INTO `oauth_client_details` (
	`client_id`,`client_secret`,
	`resource_ids`,
	`scope`,
	`authorized_grant_types`,
	`web_server_redirect_uri`,`authorities`,
	`access_token_validity`,`refresh_token_validity`,
	`additional_information`,`autoapprove`)
	VALUES
	(
  'OVERALL_SUPER_ADMIN_APP','{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu',
	'USER_ADMIN_RESOURCE',
	'role_oauth_super_admin,role_oauth_client_admin,role_quartz_super_manager,role_quartz_super_viewer,role_quartz_core_manager,role_quartz_core_viewer,role_quartz_execution_manager,role_quartz_execution_viewer,role_quartz_event_manager,role_quartz_event_viewer,role_quartz_metadata_manager,role_quartz_metadata_viewer,role_quartz_workflow_manager,role_quartz_workflow_viewer,role_quartz_task_manager,role_quartz_task_viewer',
	'authorization_code,password,refresh_token,implicit',
	NULL,NULL,
	3600,14400,
	'{}',NULL),
  (
  'OAUTH_SUPER_ADMIN_APP','{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu',
	'USER_ADMIN_RESOURCE',
	'role_oauth_super_admin',
	'authorization_code,password,refresh_token,implicit',
	NULL,NULL,
	3600,14400,
	'{}',NULL),
  (
  'OAUTH_CLIENT_ADMIN_APP','{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu',
	'USER_ADMIN_RESOURCE',
	'role_oauth_client_admin',
	'authorization_code,password,refresh_token,implicit',
	NULL,NULL,
	3600,14400,
	'{}',NULL),
	(
  'QUARTZ_USER_APP','{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu',
	'USER_ADMIN_RESOURCE',
	'role_quartz_super_manager,role_quartz_super_viewer,role_quartz_core_manager,role_quartz_core_viewer,role_quartz_execution_manager,role_quartz_execution_viewer,role_quartz_event_manager,role_quartz_event_viewer,role_quartz_metadata_manager,role_quartz_metadata_viewer,role_quartz_workflow_manager,role_quartz_workflow_viewer,role_quartz_task_manager,role_quartz_task_viewer',
	'authorization_code,password,refresh_token,implicit',
	NULL,NULL,
	900,3600,
	'{}',NULL),
  (
  'QUARTZ_CLIENT_APP','{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu',
	'USER_ADMIN_RESOURCE',
	'role_quartz_execution_manager',
	'authorization_code,password,refresh_token,implicit',
	NULL,NULL,
	86400,345600,
	'{}',NULL);
 
CREATE TABLE `oauth_permission` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(60) UNIQUE KEY,
`insert_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp());

INSERT INTO `oauth_permission` (`name`) VALUES 
('can_create_user'),
('can_update_user'),
('can_read_user'),
('can_delete_user');

		CREATE TABLE `oauth_role` 
		(`id` INT PRIMARY KEY AUTO_INCREMENT, 
		`name` VARCHAR(60) UNIQUE KEY,
		`insert_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  		`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp());


		INSERT INTO `oauth_role` (`name`) VALUES 
		('role_quartz_super_manager'), 
		('role_quartz_super_viewer'),
		('role_quartz_execution_manager'),
		('role_quartz_execution_viewer'),
		('role_quartz_metadata_manager'),
		('role_quartz_metadata_viewer'),
		('role_oauth_super_admin'),
		('role_oauth_client_admin');

	CREATE TABLE `oauth_permission_role`(
    `permission_id` INT,
    `role_id` INT,
    `insert_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  	`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    FOREIGN KEY(`permission_id`) REFERENCES `oauth_permission`(`id`),
    FOREIGN KEY(`role_id`) REFERENCES `oauth_role`(`id`));
    
    INSERT INTO `oauth_permission_role` (`permission_id`, `role_id`) VALUES 
    (1,1),
	(1,2),
	(1,3),
	(1,4),
	(1,5),
	(1,6),
	(1,7),
	(1,8),
	(2,1),
	(2,2),
	(2,3),
	(2,4),
	(2,5),
	(2,6),
	(2,7),
	(2,8),
	(3,1),
	(3,2),
	(3,3),
	(3,4),
	(3,5),
	(3,6),
	(3,7),
	(3,8),
	(4,1),
	(4,2),
	(4,3),
	(4,4),
	(4,5),
	(4,6),
	(4,7),
	(4,8);


	CREATE TABLE `oauth_user` (
    `id`  INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(100) UNIQUE KEY NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `client` VARCHAR(255) NOT NULL,
    `enabled` BIT(1) NOT NULL,
    `account_expired` BIT(1) NOT NULL,
    `credentials_expired` BIT(1) NOT NULL,
    `account_locked` BIT(1) NOT NULL,
    `insert_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  	`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    FOREIGN KEY(`client`) REFERENCES `oauth_client_details`(`client_id`));

    INSERT INTO `oauth_user` (
      `username`,`password`,
      `email`, `client`,`enabled`,`account_expired`,`credentials_expired`,`account_locked`) VALUES 
    ( 'user_quartz_super_manager'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_quartz_super_manager@ansicon.com', 'QUARTZ_USER_APP'	,1,0,0,0),
    ( 'user_quartz_super_viewer'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_quartz_super_viewer@ansicon.com', 'QUARTZ_USER_APP'	,1,0,0,0),
    ( 'user_quartz_execution_manager'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_quartz_execution_manager@ansicon.com', 'QUARTZ_USER_APP'	,1,0,0,0),
    ( 'user_quartz_execution_viewer'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_quartz_execution_viewer@ansicon.com', 'QUARTZ_USER_APP'	,1,0,0,0),
    ( 'user_quartz_metadata_manager'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_quartz_metadata_manager@ansicon.com', 'QUARTZ_USER_APP'	,1,0,0,0),
    ( 'user_quartz_metadata_viewer'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_quartz_metadata_viewer@ansicon.com', 'QUARTZ_USER_APP'	,1,0,0,0),
    ( 'user_oauth_super_admin'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_oauth_super_admin@ansicon.com', 'OAUTH_SUPER_ADMIN_APP'	,1,0,0,0),
    ( 'user_oauth_client_admin'	,	'{bcrypt}$2a$10$6xGQG60cXewsSQMFb1zeaehAJu5k5hqPJiHHFXJGcn85yvlCcbMMu'	,	'user_oauth_client_admin@ansicon.com', 'OAUTH_CLIENT_ADMIN_APP'	,1,0,0,0);


	CREATE TABLE `oauth_role_user` (
	`role_id` INT,FOREIGN KEY(`role_id`) REFERENCES `oauth_role`(`id`),
    `user_id` INT, FOREIGN KEY(`user_id`) REFERENCES `oauth_user`(`id`),
    `insert_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  	`update_timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp());


    INSERT INTO `oauth_role_user` (`role_id`, `user_id`)
    VALUES
    ( 1 , 1 ),
	( 2 , 2 ),
	( 3 , 3 ),
	( 4 , 4 ),
	( 5 , 5 ),
	( 6 , 6 ),
	( 7 , 7 ),
	( 8 , 8 );

create table if not exists oauth_client_token (
  token_id VARCHAR(256),
  token LONG VARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256)
);

create table if not exists oauth_access_token (
  token_id VARCHAR(256),
  token LONG VARBINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication LONG VARBINARY,
  refresh_token VARCHAR(256)
);

create table if not exists oauth_refresh_token (
  token_id VARCHAR(256),
  token LONG VARBINARY,
  authentication LONG VARBINARY
);

create table if not exists oauth_code (
  code VARCHAR(256), authentication LONG VARBINARY
);

create table if not exists oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP NULL,
	lastModifiedAt TIMESTAMP NULL
);
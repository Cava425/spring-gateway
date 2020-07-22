
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
`token_id` varchar(255) DEFAULT NULL COMMENT '加密的access_token的值',
`token` longblob COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
`authentication_id` varchar(255) DEFAULT NULL COMMENT '加密过的username,client_id,scope',
`user_name` varchar(255) DEFAULT NULL COMMENT '登录的用户名',
`client_id` varchar(255) DEFAULT NULL COMMENT '客户端ID',
`authentication` longblob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据',
`refresh_token` varchar(255) DEFAULT NULL COMMENT '加密的refresh_token的值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals` (
`userId` varchar(255) DEFAULT NULL COMMENT '登录的用户名',
`clientId` varchar(255) DEFAULT NULL COMMENT '客户端ID',
`scope` varchar(255) DEFAULT NULL COMMENT '申请的权限范围',
`status` varchar(10) DEFAULT NULL COMMENT '状态（Approve或Deny）',
`expiresAt` datetime DEFAULT NULL COMMENT '过期时间',
`lastModifiedAt` datetime DEFAULT NULL COMMENT '最终修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
`client_id` varchar(255) NOT NULL COMMENT '客户端ID',
`resource_ids` varchar(255) DEFAULT NULL COMMENT '资源ID集合,多个资源时用逗号(,)分隔',
`client_secret` varchar(255) DEFAULT NULL COMMENT '客户端密匙',
`scope` varchar(255) DEFAULT NULL COMMENT '客户端申请的权限范围',
`authorized_grant_types` varchar(255) DEFAULT NULL COMMENT '客户端支持的grant_type',
`web_server_redirect_uri` varchar(255) DEFAULT NULL COMMENT '重定向URI',
`authorities` varchar(255) DEFAULT NULL COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
`access_token_validity` int(11) DEFAULT NULL COMMENT '访问令牌有效时间值(单位:秒)',
`refresh_token_validity` int(11) DEFAULT NULL COMMENT '更新令牌有效时间值(单位:秒)',
`additional_information` varchar(255) DEFAULT NULL COMMENT '预留字段',
`autoapprove` varchar(255) DEFAULT NULL COMMENT '用户是否自动Approval操作'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
`token_id` varchar(255) DEFAULT NULL COMMENT '加密的access_token值',
`token` longblob COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
`authentication_id` varchar(255) DEFAULT NULL COMMENT '加密过的username,client_id,scope',
`user_name` varchar(255) DEFAULT NULL COMMENT '登录的用户名',
`client_id` varchar(255) DEFAULT NULL COMMENT '客户端ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
`code` varchar(255) DEFAULT NULL COMMENT '授权码(未加密)',
`authentication` varbinary(255) DEFAULT NULL COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
`token_id` varchar(255) DEFAULT NULL COMMENT '加密过的refresh_token的值',
`token` longblob COMMENT 'OAuth2RefreshToken.java对象序列化后的二进制数据 ',
`authentication` longblob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
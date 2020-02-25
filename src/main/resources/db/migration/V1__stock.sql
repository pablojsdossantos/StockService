CREATE TABLE `update_stock_cmd` (
  `id` VARCHAR(128) NOT NULL,
  `payload` MEDIUMTEXT NULL,
  `create_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_dt` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stock_history` (
  `stock_code` VARCHAR(128) NOT NULL,
  `reference_date` DATE NOT NULL,
  `price` DOUBLE NULL,
  `price_var_1d` DOUBLE NULL,
  `price_var_1m` DOUBLE NULL,
  `price_var_3m` DOUBLE NULL,
  `price_var_6m` DOUBLE NULL,
  `price_var_1y` DOUBLE NULL,
  `price_var_5y` DOUBLE NULL,
  `daily_liquidity` INT NULL,
  `liquidity_var_1d` DOUBLE NULL,
  `liquidity_var_1m` DOUBLE NULL,
  `liquidity_var_3m` DOUBLE NULL,
  `liquidity_var_6m` DOUBLE NULL,
  `liquidity_var_1y` DOUBLE NULL,
  `liquidity_var_5y` DOUBLE NULL,
  `dividend` DOUBLE NULL,
  `dividend_var_1d` DOUBLE NULL,
  `dividend_var_1m` DOUBLE NULL,
  `dividend_var_3m` DOUBLE NULL,
  `dividend_var_6m` DOUBLE NULL,
  `dividend_var_1y` DOUBLE NULL,
  `dividend_var_5y` DOUBLE NULL,
  `dividend_yield` DOUBLE NULL,
  `div_yield_var_1d` DOUBLE NULL,
  `div_yield_var_1m` DOUBLE NULL,
  `div_yield_var_3m` DOUBLE NULL,
  `div_yield_var_6m` DOUBLE NULL,
  `div_yield_var_1y` DOUBLE NULL,
  `div_yield_var_5y` DOUBLE NULL,
  `real_estate_asset_count` INT NULL,
  `estate_var_1d` DOUBLE NULL,
  `estate_var_1m` DOUBLE NULL,
  `estate_var_3m` DOUBLE NULL,
  `estate_var_6m` DOUBLE NULL,
  `estate_var_1y` DOUBLE NULL,
  `estate_var_5y` DOUBLE NULL,
  `create_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `update_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stock_code`, `reference_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `stock` (
  `stock_code` VARCHAR(20) NOT NULL,
  `category` VARCHAR(128) NULL,
  `current_price` DOUBLE NULL,
  `daily_liquidity` INT NULL,
  `dividend` DOUBLE NULL,
  `dividend_yield` DOUBLE NULL,
  `real_estate_asset_count` INT NULL,
  `last_update` DATE NULL,
  `create_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `update_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stock_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE `stock_history`
  DROP COLUMN `price_var_1d`,
  DROP COLUMN `price_var_1m`,
  DROP COLUMN `price_var_3m`,
  DROP COLUMN `price_var_6m`,
  DROP COLUMN `price_var_1y`,
  DROP COLUMN `price_var_5y`,
  DROP COLUMN `liquidity_var_1d`,
  DROP COLUMN `liquidity_var_1m`,
  DROP COLUMN `liquidity_var_3m`,
  DROP COLUMN `liquidity_var_6m`,
  DROP COLUMN `liquidity_var_1y`,
  DROP COLUMN `liquidity_var_5y`,
  DROP COLUMN `dividend_var_1d`,
  DROP COLUMN `dividend_var_1m`,
  DROP COLUMN `dividend_var_3m`,
  DROP COLUMN `dividend_var_6m`,
  DROP COLUMN `dividend_var_1y`,
  DROP COLUMN `dividend_var_5y`,
  DROP COLUMN `div_yield_var_1d`,
  DROP COLUMN `div_yield_var_1m`,
  DROP COLUMN `div_yield_var_3m`,
  DROP COLUMN `div_yield_var_6m`,
  DROP COLUMN `div_yield_var_1y`,
  DROP COLUMN `div_yield_var_5y`,
  DROP COLUMN `estate_var_1d`,
  DROP COLUMN `estate_var_1m`,
  DROP COLUMN `estate_var_3m`,
  DROP COLUMN `estate_var_6m`,
  DROP COLUMN `estate_var_1y`,
  DROP COLUMN `estate_var_5y`;

CREATE TABLE `stock_history_var` (
  `stock_code` VARCHAR(128) NOT NULL,
  `reference_date` DATE NULL,

  `price_var_1d` DOUBLE NULL,
  `price_var_1d_ref` DATE NULL,

  `price_var_1m` DOUBLE NULL,
  `price_var_1m_ref` DATE NULL,

  `price_var_3m` DOUBLE NULL,
  `price_var_3m_ref` DATE NULL,

  `price_var_6m` DOUBLE NULL,
  `price_var_6m_ref` DATE NULL,

  `price_var_1y` DOUBLE NULL,
  `price_var_1y_ref` DATE NULL,

  `price_var_5y` DOUBLE NULL,
  `price_var_5y_ref` DATE NULL,

  `liquidity_var_1d` DOUBLE NULL,
  `liquidity_var_1d_ref` DATE NULL,

  `liquidity_var_1m` DOUBLE NULL,
  `liquidity_var_1m_ref` DATE NULL,

  `liquidity_var_3m` DOUBLE NULL,
  `liquidity_var_3m_ref` DATE NULL,

  `liquidity_var_6m` DOUBLE NULL,
  `liquidity_var_6m_ref` DATE NULL,

  `liquidity_var_1y` DOUBLE NULL,
  `liquidity_var_1y_ref` DATE NULL,

  `liquidity_var_5y` DOUBLE NULL,
  `liquidity_var_5y_ref` DATE NULL,

  `dividend_var_1d` DOUBLE NULL,
  `dividend_var_1d_ref` DATE NULL,

  `dividend_var_1m` DOUBLE NULL,
  `dividend_var_1m_ref` DATE NULL,

  `dividend_var_3m` DOUBLE NULL,
  `dividend_var_3m_ref` DATE NULL,

  `dividend_var_6m` DOUBLE NULL,
  `dividend_var_6m_ref` DATE NULL,

  `dividend_var_1y` DOUBLE NULL,
  `dividend_var_1y_ref` DATE NULL,

  `dividend_var_5y` DOUBLE NULL,
  `dividend_var_5y_ref` DATE NULL,

  `div_yield_var_1d` DOUBLE NULL,
  `div_yield_var_1d_ref` DATE NULL,

  `div_yield_var_1m` DOUBLE NULL,
  `div_yield_var_1m_ref` DATE NULL,

  `div_yield_var_3m` DOUBLE NULL,
  `div_yield_var_3m_ref` DATE NULL,

  `div_yield_var_6m` DOUBLE NULL,
  `div_yield_var_6m_ref` DATE NULL,

  `div_yield_var_1y` DOUBLE NULL,
  `div_yield_var_1y_ref` DATE NULL,

  `div_yield_var_5y` DOUBLE NULL,
  `div_yield_var_5y_ref` DATE NULL,

  `estate_var_1d` DOUBLE NULL,
  `estate_var_1d_ref` DATE NULL,

  `estate_var_1m` DOUBLE NULL,
  `estate_var_1m_ref` DATE NULL,

  `estate_var_3m` DOUBLE NULL,
  `estate_var_3m_ref` DATE NULL,

  `estate_var_6m` DOUBLE NULL,
  `estate_var_6m_ref` DATE NULL,

  `estate_var_1y` DOUBLE NULL,
  `estate_var_1y_ref` DATE NULL,

  `estate_var_5y` DOUBLE NULL,
  `estate_var_5y_ref` DATE NULL,

  `create_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `update_dt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stock_code`, `reference_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
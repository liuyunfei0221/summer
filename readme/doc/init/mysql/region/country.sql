USE
base;

INSERT INTO `country`(`id`, `name`, `native_name`, `numeric_code`, `country_code`, `phone_code`, `capital`,
                                  `currency`, `currency_symbol`, `top_level_domain`, `region`, `emoji`, `emojiU`,
                                  `status`, `create_time`, `update_time`)
VALUES (1, 'Afghanistan', 'افغانستان', '004', 'AF', '93', 'Kabul', 'AFN', '؋', '.af', 'Asia', '🇦🇫', 'U+1F1E6 U+1F1EB',
        1, 1, 1),
       (2, 'Aland Islands', 'Åland', '248', 'AX', '+358-18', 'Mariehamn', 'EUR', '€', '.ax', 'Europe', '🇦🇽',
        'U+1F1E6 U+1F1FD', 1, 1, 1),
       (3, 'Albania', 'Shqipëria', '008', 'AL', '355', 'Tirana', 'ALL', 'Lek', '.al', 'Europe', '🇦🇱',
        'U+1F1E6 U+1F1F1', 1, 1, 1),
       (4, 'Algeria', 'الجزائر', '012', 'DZ', '213', 'Algiers', 'DZD', 'دج', '.dz', 'Africa', '🇩🇿', 'U+1F1E9 U+1F1FF',
        1, 1, 1),
       (5, 'American Samoa', 'American Samoa', '016', 'AS', '+1-684', 'Pago Pago', 'USD', '$', '.as', 'Oceania', '🇦🇸',
        'U+1F1E6 U+1F1F8', 1, 1, 1),
       (6, 'Andorra', 'Andorra', '020', 'AD', '376', 'Andorra la Vella', 'EUR', '€', '.ad', 'Europe', '🇦🇩',
        'U+1F1E6 U+1F1E9', 1, 1, 1),
       (7, 'Angola', 'Angola', '024', 'AO', '244', 'Luanda', 'AOA', 'Kz', '.ao', 'Africa', '🇦🇴', 'U+1F1E6 U+1F1F4', 1,
        1, 1),
       (8, 'Anguilla', 'Anguilla', '660', 'AI', '+1-264', 'The Valley', 'XCD', '$', '.ai', 'Americas', '🇦🇮',
        'U+1F1E6 U+1F1EE', 1, 1, 1),
       (9, 'Antarctica', 'Antarctica', '010', 'AQ', '672', '', 'AAD', '$', '.aq', 'Polar', '🇦🇶', 'U+1F1E6 U+1F1F6', 1,
        1, 1),
       (10, 'Antigua And Barbuda', 'Antigua and Barbuda', '028', 'AG', '+1-268', 'St. John`s', 'XCD', '$', '.ag',
        'Americas', '🇦🇬', 'U+1F1E6 U+1F1EC', 1, 1, 1),
       (11, 'Argentina', 'Argentina', '032', 'AR', '54', 'Buenos Aires', 'ARS', '$', '.ar', 'Americas', '🇦🇷',
        'U+1F1E6 U+1F1F7', 1, 1, 1),
       (12, 'Armenia', 'Հայաստան', '051', 'AM', '374', 'Yerevan', 'AMD', '֏', '.am', 'Asia', '🇦🇲', 'U+1F1E6 U+1F1F2',
        1, 1, 1),
       (13, 'Aruba', 'Aruba', '533', 'AW', '297', 'Oranjestad', 'AWG', 'ƒ', '.aw', 'Americas', '🇦🇼',
        'U+1F1E6 U+1F1FC', 1, 1, 1),
       (14, 'Australia', 'Australia', '036', 'AU', '61', 'Canberra', 'AUD', '$', '.au', 'Oceania', '🇦🇺',
        'U+1F1E6 U+1F1FA', 1, 1, 1),
       (15, 'Austria', 'Österreich', '040', 'AT', '43', 'Vienna', 'EUR', '€', '.at', 'Europe', '🇦🇹',
        'U+1F1E6 U+1F1F9', 1, 1, 1),
       (16, 'Azerbaijan', 'Azərbaycan', '031', 'AZ', '994', 'Baku', 'AZN', 'm', '.az', 'Asia', '🇦🇿',
        'U+1F1E6 U+1F1FF', 1, 1, 1),
       (17, 'Bahamas The', 'Bahamas', '044', 'BS', '+1-242', 'Nassau', 'BSD', 'B$', '.bs', 'Americas', '🇧🇸',
        'U+1F1E7 U+1F1F8', 1, 1, 1),
       (18, 'Bahrain', '‏البحرين', '048', 'BH', '973', 'Manama', 'BHD', '.د.ب', '.bh', 'Asia', '🇧🇭',
        'U+1F1E7 U+1F1ED', 1, 1, 1),
       (19, 'Bangladesh', 'Bangladesh', '050', 'BD', '880', 'Dhaka', 'BDT', '৳', '.bd', 'Asia', '🇧🇩',
        'U+1F1E7 U+1F1E9', 1, 1, 1),
       (20, 'Barbados', 'Barbados', '052', 'BB', '+1-246', 'Bridgetown', 'BBD', 'Bds$', '.bb', 'Americas', '🇧🇧',
        'U+1F1E7 U+1F1E7', 1, 1, 1),
       (21, 'Belarus', 'Белару́сь', '112', 'BY', '375', 'Minsk', 'BYN', 'Br', '.by', 'Europe', '🇧🇾',
        'U+1F1E7 U+1F1FE', 1, 1, 1),
       (22, 'Belgium', 'België', '056', 'BE', '32', 'Brussels', 'EUR', '€', '.be', 'Europe', '🇧🇪', 'U+1F1E7 U+1F1EA',
        1, 1, 1),
       (23, 'Belize', 'Belize', '084', 'BZ', '501', 'Belmopan', 'BZD', '$', '.bz', 'Americas', '🇧🇿',
        'U+1F1E7 U+1F1FF', 1, 1, 1),
       (24, 'Benin', 'Bénin', '204', 'BJ', '229', 'Porto-Novo', 'XOF', 'CFA', '.bj', 'Africa', '🇧🇯',
        'U+1F1E7 U+1F1EF', 1, 1, 1),
       (25, 'Bermuda', 'Bermuda', '060', 'BM', '+1-441', 'Hamilton', 'BMD', '$', '.bm', 'Americas', '🇧🇲',
        'U+1F1E7 U+1F1F2', 1, 1, 1),
       (26, 'Bhutan', 'ʼbrug-yul', '064', 'BT', '975', 'Thimphu', 'BTN', 'Nu.', '.bt', 'Asia', '🇧🇹',
        'U+1F1E7 U+1F1F9', 1, 1, 1),
       (27, 'Bolivia', 'Bolivia', '068', 'BO', '591', 'Sucre', 'BOB', 'Bs.', '.bo', 'Americas', '🇧🇴',
        'U+1F1E7 U+1F1F4', 1, 1, 1),
       (28, 'Bosnia and Herzegovina', 'Bosna i Hercegovina', '070', 'BA', '387', 'Sarajevo', 'BAM', 'KM', '.ba',
        'Europe', '🇧🇦', 'U+1F1E7 U+1F1E6', 1, 1, 1),
       (29, 'Botswana', 'Botswana', '072', 'BW', '267', 'Gaborone', 'BWP', 'P', '.bw', 'Africa', '🇧🇼',
        'U+1F1E7 U+1F1FC', 1, 1, 1),
       (30, 'Bouvet Island', 'Bouvetøya', '074', 'BV', '0055', '', 'NOK', 'kr', '.bv', '', '🇧🇻', 'U+1F1E7 U+1F1FB', 1,
        1, 1),
       (31, 'Brazil', 'Brasil', '076', 'BR', '55', 'Brasilia', 'BRL', 'R$', '.br', 'Americas', '🇧🇷',
        'U+1F1E7 U+1F1F7', 1, 1, 1),
       (32, 'British Indian Ocean Territory', 'British Indian Ocean Territory', '086', 'IO', '246', 'Diego Garcia',
        'USD', '$', '.io', 'Africa', '🇮🇴', 'U+1F1EE U+1F1F4', 1, 1, 1),
       (33, 'Brunei', 'Negara Brunei Darussalam', '096', 'BN', '673', 'Bandar Seri Begawan', 'BND', 'B$', '.bn', 'Asia',
        '🇧🇳', 'U+1F1E7 U+1F1F3', 1, 1, 1),
       (34, 'Bulgaria', 'България', '100', 'BG', '359', 'Sofia', 'BGN', 'Лв.', '.bg', 'Europe', '🇧🇬',
        'U+1F1E7 U+1F1EC', 1, 1, 1),
       (35, 'Burkina Faso', 'Burkina Faso', '854', 'BF', '226', 'Ouagadougou', 'XOF', 'CFA', '.bf', 'Africa', '🇧🇫',
        'U+1F1E7 U+1F1EB', 1, 1, 1),
       (36, 'Burundi', 'Burundi', '108', 'BI', '257', 'Bujumbura', 'BIF', 'FBu', '.bi', 'Africa', '🇧🇮',
        'U+1F1E7 U+1F1EE', 1, 1, 1),
       (37, 'Cambodia', 'Kâmpŭchéa', '116', 'KH', '855', 'Phnom Penh', 'KHR', 'KHR', '.kh', 'Asia', '🇰🇭',
        'U+1F1F0 U+1F1ED', 1, 1, 1),
       (38, 'Cameroon', 'Cameroon', '120', 'CM', '237', 'Yaounde', 'XAF', 'FCFA', '.cm', 'Africa', '🇨🇲',
        'U+1F1E8 U+1F1F2', 1, 1, 1),
       (39, 'Canada', 'Canada', '124', 'CA', '1', 'Ottawa', 'CAD', '$', '.ca', 'Americas', '🇨🇦', 'U+1F1E8 U+1F1E6', 1,
        1, 1),
       (40, 'Cape Verde', 'Cabo Verde', '132', 'CV', '238', 'Praia', 'CVE', '$', '.cv', 'Africa', '🇨🇻',
        'U+1F1E8 U+1F1FB', 1, 1, 1),
       (41, 'Cayman Islands', 'Cayman Islands', '136', 'KY', '+1-345', 'George Town', 'KYD', '$', '.ky', 'Americas',
        '🇰🇾', 'U+1F1F0 U+1F1FE', 1, 1, 1),
       (42, 'Central African Republic', 'Ködörösêse tî Bêafrîka', '140', 'CF', '236', 'Bangui', 'XAF', 'FCFA', '.cf',
        'Africa', '🇨🇫', 'U+1F1E8 U+1F1EB', 1, 1, 1),
       (43, 'Chad', 'Tchad', '148', 'TD', '235', 'N`Djamena', 'XAF', 'FCFA', '.td', 'Africa', '🇹🇩', 'U+1F1F9 U+1F1E9',
        1, 1, 1),
       (44, 'Chile', 'Chile', '152', 'CL', '56', 'Santiago', 'CLP', '$', '.cl', 'Americas', '🇨🇱', 'U+1F1E8 U+1F1F1',
        1, 1, 1),
       (45, 'China', '中国', '156', 'CN', '86', 'Beijing', 'CNY', '¥', '.cn', 'Asia', '🇨🇳', 'U+1F1E8 U+1F1F3', 1, 1, 1),
       (46, 'Christmas Island', 'Christmas Island', '162', 'CX', '61', 'Flying Fish Cove', 'AUD', '$', '.cx', 'Oceania',
        '🇨🇽', 'U+1F1E8 U+1F1FD', 1, 1, 1),
       (47, 'Cocos (Keeling) Islands', 'Cocos (Keeling) Islands', '166', 'CC', '61', 'West Island', 'AUD', '$', '.cc',
        'Oceania', '🇨🇨', 'U+1F1E8 U+1F1E8', 1, 1, 1),
       (48, 'Colombia', 'Colombia', '170', 'CO', '57', 'Bogota', 'COP', '$', '.co', 'Americas', '🇨🇴',
        'U+1F1E8 U+1F1F4', 1, 1, 1),
       (49, 'Comoros', 'Komori', '174', 'KM', '269', 'Moroni', 'KMF', 'CF', '.km', 'Africa', '🇰🇲', 'U+1F1F0 U+1F1F2',
        1, 1, 1),
       (50, 'Congo', 'République du Congo', '178', 'CG', '242', 'Brazzaville', 'XAF', 'FC', '.cg', 'Africa', '🇨🇬',
        'U+1F1E8 U+1F1EC', 1, 1, 1),
       (51, 'Democratic Republic of the Congo', 'République démocratique du Congo', '180', 'CD', '243', 'Kinshasa',
        'CDF', 'FC', '.cd', 'Africa', '🇨🇩', 'U+1F1E8 U+1F1E9', 1, 1, 1),
       (52, 'Cook Islands', 'Cook Islands', '184', 'CK', '682', 'Avarua', 'NZD', '$', '.ck', 'Oceania', '🇨🇰',
        'U+1F1E8 U+1F1F0', 1, 1, 1),
       (53, 'Costa Rica', 'Costa Rica', '188', 'CR', '506', 'San Jose', 'CRC', '₡', '.cr', 'Americas', '🇨🇷',
        'U+1F1E8 U+1F1F7', 1, 1, 1),
       (54, 'Cote D`Ivoire (Ivory Coast)', NULL, '384', 'CI', '225', 'Yamoussoukro', 'XOF', 'CFA', '.ci', 'Africa',
        '🇨🇮', 'U+1F1E8 U+1F1EE', 1, 1, 1),
       (55, 'Croatia', 'Hrvatska', '191', 'HR', '385', 'Zagreb', 'HRK', 'kn', '.hr', 'Europe', '🇭🇷',
        'U+1F1ED U+1F1F7', 1, 1, 1),
       (56, 'Cuba', 'Cuba', '192', 'CU', '53', 'Havana', 'CUP', '$', '.cu', 'Americas', '🇨🇺', 'U+1F1E8 U+1F1FA', 1, 1,
        1),
       (57, 'Cyprus', 'Κύπρος', '196', 'CY', '357', 'Nicosia', 'EUR', '€', '.cy', 'Europe', '🇨🇾', 'U+1F1E8 U+1F1FE',
        1, 1, 1),
       (58, 'Czech Republic', 'Česká republika', '203', 'CZ', '420', 'Prague', 'CZK', 'Kč', '.cz', 'Europe', '🇨🇿',
        'U+1F1E8 U+1F1FF', 1, 1, 1),
       (59, 'Denmark', 'Danmark', '208', 'DK', '45', 'Copenhagen', 'DKK', 'Kr.', '.dk', 'Europe', '🇩🇰',
        'U+1F1E9 U+1F1F0', 1, 1, 1),
       (60, 'Djibouti', 'Djibouti', '262', 'DJ', '253', 'Djibouti', 'DJF', 'Fdj', '.dj', 'Africa', '🇩🇯',
        'U+1F1E9 U+1F1EF', 1, 1, 1),
       (61, 'Dominica', 'Dominica', '212', 'DM', '+1-767', 'Roseau', 'XCD', '$', '.dm', 'Americas', '🇩🇲',
        'U+1F1E9 U+1F1F2', 1, 1, 1),
       (62, 'Dominican Republic', 'República Dominicana', '214', 'DO', '+1-809 and 1-829', 'Santo Domingo', 'DOP', '$',
        '.do', 'Americas', '🇩🇴', 'U+1F1E9 U+1F1F4', 1, 1, 1),
       (63, 'East Timor', 'Timor-Leste', '626', 'TL', '670', 'Dili', 'USD', '$', '.tl', 'Asia', '🇹🇱',
        'U+1F1F9 U+1F1F1', 1, 1, 1),
       (64, 'Ecuador', 'Ecuador', '218', 'EC', '593', 'Quito', 'USD', '$', '.ec', 'Americas', '🇪🇨', 'U+1F1EA U+1F1E8',
        1, 1, 1),
       (65, 'Egypt', 'مصر‎', '818', 'EG', '20', 'Cairo', 'EGP', 'ج.م', '.eg', 'Africa', '🇪🇬', 'U+1F1EA U+1F1EC', 1, 1,
        1),
       (66, 'El Salvador', 'El Salvador', '222', 'SV', '503', 'San Salvador', 'USD', '$', '.sv', 'Americas', '🇸🇻',
        'U+1F1F8 U+1F1FB', 1, 1, 1),
       (67, 'Equatorial Guinea', 'Guinea Ecuatorial', '226', 'GQ', '240', 'Malabo', 'XAF', 'FCFA', '.gq', 'Africa',
        '🇬🇶', 'U+1F1EC U+1F1F6', 1, 1, 1),
       (68, 'Eritrea', 'ኤርትራ', '232', 'ER', '291', 'Asmara', 'ERN', 'Nfk', '.er', 'Africa', '🇪🇷', 'U+1F1EA U+1F1F7',
        1, 1, 1),
       (69, 'Estonia', 'Eesti', '233', 'EE', '372', 'Tallinn', 'EUR', '€', '.ee', 'Europe', '🇪🇪', 'U+1F1EA U+1F1EA',
        1, 1, 1),
       (70, 'Ethiopia', 'ኢትዮጵያ', '231', 'ET', '251', 'Addis Ababa', 'ETB', 'Nkf', '.et', 'Africa', '🇪🇹',
        'U+1F1EA U+1F1F9', 1, 1, 1),
       (71, 'Falkland Islands', 'Falkland Islands', '238', 'FK', '500', 'Stanley', 'FKP', '£', '.fk', 'Americas',
        '🇫🇰', 'U+1F1EB U+1F1F0', 1, 1, 1),
       (72, 'Faroe Islands', 'Føroyar', '234', 'FO', '298', 'Torshavn', 'DKK', 'Kr.', '.fo', 'Europe', '🇫🇴',
        'U+1F1EB U+1F1F4', 1, 1, 1),
       (73, 'Fiji Islands', 'Fiji', '242', 'FJ', '679', 'Suva', 'FJD', 'FJ$', '.fj', 'Oceania', '🇫🇯',
        'U+1F1EB U+1F1EF', 1, 1, 1),
       (74, 'Finland', 'Suomi', '246', 'FI', '358', 'Helsinki', 'EUR', '€', '.fi', 'Europe', '🇫🇮', 'U+1F1EB U+1F1EE',
        1, 1, 1),
       (75, 'France', 'France', '250', 'FR', '33', 'Paris', 'EUR', '€', '.fr', 'Europe', '🇫🇷', 'U+1F1EB U+1F1F7', 1,
        1, 1),
       (76, 'French Guiana', 'Guyane française', '254', 'GF', '594', 'Cayenne', 'EUR', '€', '.gf', 'Americas', '🇬🇫',
        'U+1F1EC U+1F1EB', 1, 1, 1),
       (77, 'French Polynesia', 'Polynésie française', '258', 'PF', '689', 'Papeete', 'XPF', '₣', '.pf', 'Oceania',
        '🇵🇫', 'U+1F1F5 U+1F1EB', 1, 1, 1),
       (78, 'French Southern Territories', 'Territoire des Terres australes et antarctiques fr', '260', 'TF', '262',
        'Port-aux-Francais', 'EUR', '€', '.tf', 'Africa', '🇹🇫', 'U+1F1F9 U+1F1EB', 1, 1, 1),
       (79, 'Gabon', 'Gabon', '266', 'GA', '241', 'Libreville', 'XAF', 'FCFA', '.ga', 'Africa', '🇬🇦',
        'U+1F1EC U+1F1E6', 1, 1, 1),
       (80, 'Gambia The', 'Gambia', '270', 'GM', '220', 'Banjul', 'GMD', 'D', '.gm', 'Africa', '🇬🇲',
        'U+1F1EC U+1F1F2', 1, 1, 1),
       (81, 'Georgia', 'საქართველო', '268', 'GE', '995', 'Tbilisi', 'GEL', 'ლ', '.ge', 'Asia', '🇬🇪',
        'U+1F1EC U+1F1EA', 1, 1, 1),
       (82, 'Germany', 'Deutschland', '276', 'DE', '49', 'Berlin', 'EUR', '€', '.de', 'Europe', '🇩🇪',
        'U+1F1E9 U+1F1EA', 1, 1, 1),
       (83, 'Ghana', 'Ghana', '288', 'GH', '233', 'Accra', 'GHS', 'GH₵', '.gh', 'Africa', '🇬🇭', 'U+1F1EC U+1F1ED', 1,
        1, 1),
       (84, 'Gibraltar', 'Gibraltar', '292', 'GI', '350', 'Gibraltar', 'GIP', '£', '.gi', 'Europe', '🇬🇮',
        'U+1F1EC U+1F1EE', 1, 1, 1),
       (85, 'Greece', 'Ελλάδα', '300', 'GR', '30', 'Athens', 'EUR', '€', '.gr', 'Europe', '🇬🇷', 'U+1F1EC U+1F1F7', 1,
        1, 1),
       (86, 'Greenland', 'Kalaallit Nunaat', '304', 'GL', '299', 'Nuuk', 'DKK', 'Kr.', '.gl', 'Americas', '🇬🇱',
        'U+1F1EC U+1F1F1', 1, 1, 1),
       (87, 'Grenada', 'Grenada', '308', 'GD', '+1-473', 'St. George`s', 'XCD', '$', '.gd', 'Americas', '🇬🇩',
        'U+1F1EC U+1F1E9', 1, 1, 1),
       (88, 'Guadeloupe', 'Guadeloupe', '312', 'GP', '590', 'Basse-Terre', 'EUR', '€', '.gp', 'Americas', '🇬🇵',
        'U+1F1EC U+1F1F5', 1, 1, 1),
       (89, 'Guam', 'Guam', '316', 'GU', '+1-671', 'Hagatna', 'USD', '$', '.gu', 'Oceania', '🇬🇺', 'U+1F1EC U+1F1FA',
        1, 1, 1),
       (90, 'Guatemala', 'Guatemala', '320', 'GT', '502', 'Guatemala City', 'GTQ', 'Q', '.gt', 'Americas', '🇬🇹',
        'U+1F1EC U+1F1F9', 1, 1, 1),
       (91, 'Guernsey and Alderney', 'Guernsey', '831', 'GG', '+44-1481', 'St Peter Port', 'GBP', '£', '.gg', 'Europe',
        '🇬🇬', 'U+1F1EC U+1F1EC', 1, 1, 1),
       (92, 'Guinea', 'Guinée', '324', 'GN', '224', 'Conakry', 'GNF', 'FG', '.gn', 'Africa', '🇬🇳', 'U+1F1EC U+1F1F3',
        1, 1, 1),
       (93, 'Guinea-Bissau', 'Guiné-Bissau', '624', 'GW', '245', 'Bissau', 'XOF', 'CFA', '.gw', 'Africa', '🇬🇼',
        'U+1F1EC U+1F1FC', 1, 1, 1),
       (94, 'Guyana', 'Guyana', '328', 'GY', '592', 'Georgetown', 'GYD', '$', '.gy', 'Americas', '🇬🇾',
        'U+1F1EC U+1F1FE', 1, 1, 1),
       (95, 'Haiti', 'Haïti', '332', 'HT', '509', 'Port-au-Prince', 'HTG', 'G', '.ht', 'Americas', '🇭🇹',
        'U+1F1ED U+1F1F9', 1, 1, 1),
       (96, 'Heard Island and McDonald Islands', 'Heard Island and McDonald Islands', '334', 'HM', '672', '', 'AUD',
        '$', '.hm', '', '🇭🇲', 'U+1F1ED U+1F1F2', 1, 1, 1),
       (97, 'Honduras', 'Honduras', '340', 'HN', '504', 'Tegucigalpa', 'HNL', 'L', '.hn', 'Americas', '🇭🇳',
        'U+1F1ED U+1F1F3', 1, 1, 1),
       (98, 'Hong Kong S.A.R.', '香港', '344', 'HK', '852', 'Hong Kong', 'HKD', '$', '.hk', 'Asia', '🇭🇰',
        'U+1F1ED U+1F1F0', 1, 1, 1),
       (99, 'Hungary', 'Magyarország', '348', 'HU', '36', 'Budapest', 'HUF', 'Ft', '.hu', 'Europe', '🇭🇺',
        'U+1F1ED U+1F1FA', 1, 1, 1),
       (100, 'Iceland', 'Ísland', '352', 'IS', '354', 'Reykjavik', 'ISK', 'kr', '.is', 'Europe', '🇮🇸',
        'U+1F1EE U+1F1F8', 1, 1, 1),
       (101, 'India', 'भारत', '356', 'IN', '91', 'New Delhi', 'INR', '₹', '.in', 'Asia', '🇮🇳', 'U+1F1EE U+1F1F3', 1,
        1, 1),
       (102, 'Indonesia', 'Indonesia', '360', 'ID', '62', 'Jakarta', 'IDR', 'Rp', '.id', 'Asia', '🇮🇩',
        'U+1F1EE U+1F1E9', 1, 1, 1),
       (103, 'Iran', 'ایران', '364', 'IR', '98', 'Tehran', 'IRR', '﷼', '.ir', 'Asia', '🇮🇷', 'U+1F1EE U+1F1F7', 1, 1,
        1),
       (104, 'Iraq', 'العراق', '368', 'IQ', '964', 'Baghdad', 'IQD', 'د.ع', '.iq', 'Asia', '🇮🇶', 'U+1F1EE U+1F1F6', 1,
        1, 1),
       (105, 'Ireland', 'Éire', '372', 'IE', '353', 'Dublin', 'EUR', '€', '.ie', 'Europe', '🇮🇪', 'U+1F1EE U+1F1EA', 1,
        1, 1),
       (106, 'Israel', 'יִשְׂרָאֵל', '376', 'IL', '972', 'Jerusalem', 'ILS', '₪', '.il', 'Asia', '🇮🇱',
        'U+1F1EE U+1F1F1', 1, 1, 1),
       (107, 'Italy', 'Italia', '380', 'IT', '39', 'Rome', 'EUR', '€', '.it', 'Europe', '🇮🇹', 'U+1F1EE U+1F1F9', 1, 1,
        1),
       (108, 'Jamaica', 'Jamaica', '388', 'JM', '+1-876', 'Kingston', 'JMD', 'J$', '.jm', 'Americas', '🇯🇲',
        'U+1F1EF U+1F1F2', 1, 1, 1),
       (109, 'Japan', '日本', '392', 'JP', '81', 'Tokyo', 'JPY', '¥', '.jp', 'Asia', '🇯🇵', 'U+1F1EF U+1F1F5', 1, 1, 1),
       (110, 'Jersey', 'Jersey', '832', 'JE', '+44-1534', 'Saint Helier', 'GBP', '£', '.je', 'Europe', '🇯🇪',
        'U+1F1EF U+1F1EA', 1, 1, 1),
       (111, 'Jordan', 'الأردن', '400', 'JO', '962', 'Amman', 'JOD', 'ا.د', '.jo', 'Asia', '🇯🇴', 'U+1F1EF U+1F1F4', 1,
        1, 1),
       (112, 'Kazakhstan', 'Қазақстан', '398', 'KZ', '7', 'Astana', 'KZT', 'лв', '.kz', 'Asia', '🇰🇿',
        'U+1F1F0 U+1F1FF', 1, 1, 1),
       (113, 'Kenya', 'Kenya', '404', 'KE', '254', 'Nairobi', 'KES', 'KSh', '.ke', 'Africa', '🇰🇪', 'U+1F1F0 U+1F1EA',
        1, 1, 1),
       (114, 'Kiribati', 'Kiribati', '296', 'KI', '686', 'Tarawa', 'AUD', '$', '.ki', 'Oceania', '🇰🇮',
        'U+1F1F0 U+1F1EE', 1, 1, 1),
       (115, 'North Korea', '북한', '408', 'KP', '850', 'Pyongyang', 'KPW', '₩', '.kp', 'Asia', '🇰🇵', 'U+1F1F0 U+1F1F5',
        1, 1, 1),
       (116, 'South Korea', '대한민국', '410', 'KR', '82', 'Seoul', 'KRW', '₩', '.kr', 'Asia', '🇰🇷', 'U+1F1F0 U+1F1F7', 1,
        1, 1),
       (117, 'Kuwait', 'الكويت', '414', 'KW', '965', 'Kuwait City', 'KWD', 'ك.د', '.kw', 'Asia', '🇰🇼',
        'U+1F1F0 U+1F1FC', 1, 1, 1),
       (118, 'Kyrgyzstan', 'Кыргызстан', '417', 'KG', '996', 'Bishkek', 'KGS', 'лв', '.kg', 'Asia', '🇰🇬',
        'U+1F1F0 U+1F1EC', 1, 1, 1),
       (119, 'Laos', 'ສປປລາວ', '418', 'LA', '856', 'Vientiane', 'LAK', '₭', '.la', 'Asia', '🇱🇦', 'U+1F1F1 U+1F1E6', 1,
        1, 1),
       (120, 'Latvia', 'Latvija', '428', 'LV', '371', 'Riga', 'EUR', '€', '.lv', 'Europe', '🇱🇻', 'U+1F1F1 U+1F1FB', 1,
        1, 1),
       (121, 'Lebanon', 'لبنان', '422', 'LB', '961', 'Beirut', 'LBP', '£', '.lb', 'Asia', '🇱🇧', 'U+1F1F1 U+1F1E7', 1,
        1, 1),
       (122, 'Lesotho', 'Lesotho', '426', 'LS', '266', 'Maseru', 'LSL', 'L', '.ls', 'Africa', '🇱🇸', 'U+1F1F1 U+1F1F8',
        1, 1, 1),
       (123, 'Liberia', 'Liberia', '430', 'LR', '231', 'Monrovia', 'LRD', '$', '.lr', 'Africa', '🇱🇷',
        'U+1F1F1 U+1F1F7', 1, 1, 1),
       (124, 'Libya', '‏ليبيا', '434', 'LY', '218', 'Tripolis', 'LYD', 'د.ل', '.ly', 'Africa', '🇱🇾',
        'U+1F1F1 U+1F1FE', 1, 1, 1),
       (125, 'Liechtenstein', 'Liechtenstein', '438', 'LI', '423', 'Vaduz', 'CHF', 'CHf', '.li', 'Europe', '🇱🇮',
        'U+1F1F1 U+1F1EE', 1, 1, 1),
       (126, 'Lithuania', 'Lietuva', '440', 'LT', '370', 'Vilnius', 'EUR', '€', '.lt', 'Europe', '🇱🇹',
        'U+1F1F1 U+1F1F9', 1, 1, 1),
       (127, 'Luxembourg', 'Luxembourg', '442', 'LU', '352', 'Luxembourg', 'EUR', '€', '.lu', 'Europe', '🇱🇺',
        'U+1F1F1 U+1F1FA', 1, 1, 1),
       (128, 'Macau S.A.R.', '澳門', '446', 'MO', '853', 'Macao', 'MOP', '$', '.mo', 'Asia', '🇲🇴', 'U+1F1F2 U+1F1F4', 1,
        1, 1),
       (129, 'Macedonia', 'Северна Македонија', '807', 'MK', '389', 'Skopje', 'MKD', 'ден', '.mk', 'Europe', '🇲🇰',
        'U+1F1F2 U+1F1F0', 1, 1, 1),
       (130, 'Madagascar', 'Madagasikara', '450', 'MG', '261', 'Antananarivo', 'MGA', 'Ar', '.mg', 'Africa', '🇲🇬',
        'U+1F1F2 U+1F1EC', 1, 1, 1),
       (131, 'Malawi', 'Malawi', '454', 'MW', '265', 'Lilongwe', 'MWK', 'MK', '.mw', 'Africa', '🇲🇼',
        'U+1F1F2 U+1F1FC', 1, 1, 1),
       (132, 'Malaysia', 'Malaysia', '458', 'MY', '60', 'Kuala Lumpur', 'MYR', 'RM', '.my', 'Asia', '🇲🇾',
        'U+1F1F2 U+1F1FE', 1, 1, 1),
       (133, 'Maldives', 'Maldives', '462', 'MV', '960', 'Male', 'MVR', 'Rf', '.mv', 'Asia', '🇲🇻', 'U+1F1F2 U+1F1FB',
        1, 1, 1),
       (134, 'Mali', 'Mali', '466', 'ML', '223', 'Bamako', 'XOF', 'CFA', '.ml', 'Africa', '🇲🇱', 'U+1F1F2 U+1F1F1', 1,
        1, 1),
       (135, 'Malta', 'Malta', '470', 'MT', '356', 'Valletta', 'EUR', '€', '.mt', 'Europe', '🇲🇹', 'U+1F1F2 U+1F1F9',
        1, 1, 1),
       (136, 'Man (Isle of)', 'Isle of Man', '833', 'IM', '+44-1624', 'Douglas, Isle of Man', 'GBP', '£', '.im',
        'Europe', '🇮🇲', 'U+1F1EE U+1F1F2', 1, 1, 1),
       (137, 'Marshall Islands', 'M̧ajeļ', '584', 'MH', '692', 'Majuro', 'USD', '$', '.mh', 'Oceania', '🇲🇭',
        'U+1F1F2 U+1F1ED', 1, 1, 1),
       (138, 'Martinique', 'Martinique', '474', 'MQ', '596', 'Fort-de-France', 'EUR', '€', '.mq', 'Americas', '🇲🇶',
        'U+1F1F2 U+1F1F6', 1, 1, 1),
       (139, 'Mauritania', 'موريتانيا', '478', 'MR', '222', 'Nouakchott', 'MRO', 'MRU', '.mr', 'Africa', '🇲🇷',
        'U+1F1F2 U+1F1F7', 1, 1, 1),
       (140, 'Mauritius', 'Maurice', '480', 'MU', '230', 'Port Louis', 'MUR', '₨', '.mu', 'Africa', '🇲🇺',
        'U+1F1F2 U+1F1FA', 1, 1, 1),
       (141, 'Mayotte', 'Mayotte', '175', 'YT', '262', 'Mamoudzou', 'EUR', '€', '.yt', 'Africa', '🇾🇹',
        'U+1F1FE U+1F1F9', 1, 1, 1),
       (142, 'Mexico', 'México', '484', 'MX', '52', 'Mexico City', 'MXN', '$', '.mx', 'Americas', '🇲🇽',
        'U+1F1F2 U+1F1FD', 1, 1, 1),
       (143, 'Micronesia', 'Micronesia', '583', 'FM', '691', 'Palikir', 'USD', '$', '.fm', 'Oceania', '🇫🇲',
        'U+1F1EB U+1F1F2', 1, 1, 1),
       (144, 'Moldova', 'Moldova', '498', 'MD', '373', 'Chisinau', 'MDL', 'L', '.md', 'Europe', '🇲🇩',
        'U+1F1F2 U+1F1E9', 1, 1, 1),
       (145, 'Monaco', 'Monaco', '492', 'MC', '377', 'Monaco', 'EUR', '€', '.mc', 'Europe', '🇲🇨', 'U+1F1F2 U+1F1E8',
        1, 1, 1),
       (146, 'Mongolia', 'Монгол улс', '496', 'MN', '976', 'Ulan Bator', 'MNT', '₮', '.mn', 'Asia', '🇲🇳',
        'U+1F1F2 U+1F1F3', 1, 1, 1),
       (147, 'Montenegro', 'Црна Гора', '499', 'ME', '382', 'Podgorica', 'EUR', '€', '.me', 'Europe', '🇲🇪',
        'U+1F1F2 U+1F1EA', 1, 1, 1),
       (148, 'Montserrat', 'Montserrat', '500', 'MS', '+1-664', 'Plymouth', 'XCD', '$', '.ms', 'Americas', '🇲🇸',
        'U+1F1F2 U+1F1F8', 1, 1, 1),
       (149, 'Morocco', 'المغرب', '504', 'MA', '212', 'Rabat', 'MAD', 'DH', '.ma', 'Africa', '🇲🇦', 'U+1F1F2 U+1F1E6',
        1, 1, 1),
       (150, 'Mozambique', 'Moçambique', '508', 'MZ', '258', 'Maputo', 'MZN', 'MT', '.mz', 'Africa', '🇲🇿',
        'U+1F1F2 U+1F1FF', 1, 1, 1),
       (151, 'Myanmar', 'မြန်မာ', '104', 'MM', '95', 'Nay Pyi Taw', 'MMK', 'K', '.mm', 'Asia', '🇲🇲',
        'U+1F1F2 U+1F1F2', 1, 1, 1),
       (152, 'Namibia', 'Namibia', '516', 'NA', '264', 'Windhoek', 'NAD', '$', '.na', 'Africa', '🇳🇦',
        'U+1F1F3 U+1F1E6', 1, 1, 1),
       (153, 'Nauru', 'Nauru', '520', 'NR', '674', 'Yaren', 'AUD', '$', '.nr', 'Oceania', '🇳🇷', 'U+1F1F3 U+1F1F7', 1,
        1, 1),
       (154, 'Nepal', 'नपल', '524', 'NP', '977', 'Kathmandu', 'NPR', '₨', '.np', 'Asia', '🇳🇵', 'U+1F1F3 U+1F1F5', 1,
        1, 1),
       (155, 'Bonaire, Sint Eustatius and Saba', 'Caribisch Nederland', '535', 'BQ', '599', 'Kralendijk', 'USD', '$',
        '.an', 'Americas', '🇧🇶', 'U+1F1E7 U+1F1F6', 1, 1, 1),
       (156, 'Netherlands', 'Nederland', '528', 'NL', '31', 'Amsterdam', 'EUR', '€', '.nl', 'Europe', '🇳🇱',
        'U+1F1F3 U+1F1F1', 1, 1, 1),
       (157, 'New Caledonia', 'Nouvelle-Calédonie', '540', 'NC', '687', 'Noumea', 'XPF', '₣', '.nc', 'Oceania', '🇳🇨',
        'U+1F1F3 U+1F1E8', 1, 1, 1),
       (158, 'New Zealand', 'New Zealand', '554', 'NZ', '64', 'Wellington', 'NZD', '$', '.nz', 'Oceania', '🇳🇿',
        'U+1F1F3 U+1F1FF', 1, 1, 1),
       (159, 'Nicaragua', 'Nicaragua', '558', 'NI', '505', 'Managua', 'NIO', 'C$', '.ni', 'Americas', '🇳🇮',
        'U+1F1F3 U+1F1EE', 1, 1, 1),
       (160, 'Niger', 'Niger', '562', 'NE', '227', 'Niamey', 'XOF', 'CFA', '.ne', 'Africa', '🇳🇪', 'U+1F1F3 U+1F1EA',
        1, 1, 1),
       (161, 'Nigeria', 'Nigeria', '566', 'NG', '234', 'Abuja', 'NGN', '₦', '.ng', 'Africa', '🇳🇬', 'U+1F1F3 U+1F1EC',
        1, 1, 1),
       (162, 'Niue', 'Niuē', '570', 'NU', '683', 'Alofi', 'NZD', '$', '.nu', 'Oceania', '🇳🇺', 'U+1F1F3 U+1F1FA', 1, 1,
        1),
       (163, 'Norfolk Island', 'Norfolk Island', '574', 'NF', '672', 'Kingston', 'AUD', '$', '.nf', 'Oceania', '🇳🇫',
        'U+1F1F3 U+1F1EB', 1, 1, 1),
       (164, 'Northern Mariana Islands', 'Northern Mariana Islands', '580', 'MP', '+1-670', 'Saipan', 'USD', '$', '.mp',
        'Oceania', '🇲🇵', 'U+1F1F2 U+1F1F5', 1, 1, 1),
       (165, 'Norway', 'Norge', '578', 'NO', '47', 'Oslo', 'NOK', 'kr', '.no', 'Europe', '🇳🇴', 'U+1F1F3 U+1F1F4', 1,
        1, 1),
       (166, 'Oman', 'عمان', '512', 'OM', '968', 'Muscat', 'OMR', '.ع.ر', '.om', 'Asia', '🇴🇲', 'U+1F1F4 U+1F1F2', 1,
        1, 1),
       (167, 'Pakistan', 'Pakistan', '586', 'PK', '92', 'Islamabad', 'PKR', '₨', '.pk', 'Asia', '🇵🇰',
        'U+1F1F5 U+1F1F0', 1, 1, 1),
       (168, 'Palau', 'Palau', '585', 'PW', '680', 'Melekeok', 'USD', '$', '.pw', 'Oceania', '🇵🇼', 'U+1F1F5 U+1F1FC',
        1, 1, 1),
       (169, 'Palestinian Territory Occupied', 'فلسطين', '275', 'PS', '970', 'East Jerusalem', 'ILS', '₪', '.ps',
        'Asia', '🇵🇸', 'U+1F1F5 U+1F1F8', 1, 1, 1),
       (170, 'Panama', 'Panamá', '591', 'PA', '507', 'Panama City', 'PAB', 'B/.', '.pa', 'Americas', '🇵🇦',
        'U+1F1F5 U+1F1E6', 1, 1, 1),
       (171, 'Papua new Guinea', 'Papua Niugini', '598', 'PG', '675', 'Port Moresby', 'PGK', 'K', '.pg', 'Oceania',
        '🇵🇬', 'U+1F1F5 U+1F1EC', 1, 1, 1),
       (172, 'Paraguay', 'Paraguay', '600', 'PY', '595', 'Asuncion', 'PYG', '₲', '.py', 'Americas', '🇵🇾',
        'U+1F1F5 U+1F1FE', 1, 1, 1),
       (173, 'Peru', 'Perú', '604', 'PE', '51', 'Lima', 'PEN', 'S/.', '.pe', 'Americas', '🇵🇪', 'U+1F1F5 U+1F1EA', 1,
        1, 1),
       (174, 'Philippines', 'Pilipinas', '608', 'PH', '63', 'Manila', 'PHP', '₱', '.ph', 'Asia', '🇵🇭',
        'U+1F1F5 U+1F1ED', 1, 1, 1),
       (175, 'Pitcairn Island', 'Pitcairn Islands', '612', 'PN', '870', 'Adamstown', 'NZD', '$', '.pn', 'Oceania',
        '🇵🇳', 'U+1F1F5 U+1F1F3', 1, 1, 1),
       (176, 'Poland', 'Polska', '616', 'PL', '48', 'Warsaw', 'PLN', 'zł', '.pl', 'Europe', '🇵🇱', 'U+1F1F5 U+1F1F1',
        1, 1, 1),
       (177, 'Portugal', 'Portugal', '620', 'PT', '351', 'Lisbon', 'EUR', '€', '.pt', 'Europe', '🇵🇹',
        'U+1F1F5 U+1F1F9', 1, 1, 1),
       (178, 'Puerto Rico', 'Puerto Rico', '630', 'PR', '+1-787 and 1-939', 'San Juan', 'USD', '$', '.pr', 'Americas',
        '🇵🇷', 'U+1F1F5 U+1F1F7', 1, 1, 1),
       (179, 'Qatar', 'قطر', '634', 'QA', '974', 'Doha', 'QAR', 'ق.ر', '.qa', 'Asia', '🇶🇦', 'U+1F1F6 U+1F1E6', 1, 1,
        1),
       (180, 'Reunion', 'La Réunion', '638', 'RE', '262', 'Saint-Denis', 'EUR', '€', '.re', 'Africa', '🇷🇪',
        'U+1F1F7 U+1F1EA', 1, 1, 1),
       (181, 'Romania', 'România', '642', 'RO', '40', 'Bucharest', 'RON', 'lei', '.ro', 'Europe', '🇷🇴',
        'U+1F1F7 U+1F1F4', 1, 1, 1),
       (182, 'Russia', 'Россия', '643', 'RU', '7', 'Moscow', 'RUB', '₽', '.ru', 'Europe', '🇷🇺', 'U+1F1F7 U+1F1FA', 1,
        1, 1),
       (183, 'Rwanda', 'Rwanda', '646', 'RW', '250', 'Kigali', 'RWF', 'FRw', '.rw', 'Africa', '🇷🇼', 'U+1F1F7 U+1F1FC',
        1, 1, 1),
       (184, 'Saint Helena', 'Saint Helena', '654', 'SH', '290', 'Jamestown', 'SHP', '£', '.sh', 'Africa', '🇸🇭',
        'U+1F1F8 U+1F1ED', 1, 1, 1),
       (185, 'Saint Kitts And Nevis', 'Saint Kitts and Nevis', '659', 'KN', '+1-869', 'Basseterre', 'XCD', '$', '.kn',
        'Americas', '🇰🇳', 'U+1F1F0 U+1F1F3', 1, 1, 1),
       (186, 'Saint Lucia', 'Saint Lucia', '662', 'LC', '+1-758', 'Castries', 'XCD', '$', '.lc', 'Americas', '🇱🇨',
        'U+1F1F1 U+1F1E8', 1, 1, 1),
       (187, 'Saint Pierre and Miquelon', 'Saint-Pierre-et-Miquelon', '666', 'PM', '508', 'Saint-Pierre', 'EUR', '€',
        '.pm', 'Americas', '🇵🇲', 'U+1F1F5 U+1F1F2', 1, 1, 1),
       (188, 'Saint Vincent And The Grenadines', 'Saint Vincent and the Grenadines', '670', 'VC', '+1-784', 'Kingstown',
        'XCD', '$', '.vc', 'Americas', '🇻🇨', 'U+1F1FB U+1F1E8', 1, 1, 1),
       (189, 'Saint-Barthelemy', 'Saint-Barthélemy', '652', 'BL', '590', 'Gustavia', 'EUR', '€', '.bl', 'Americas',
        '🇧🇱', 'U+1F1E7 U+1F1F1', 1, 1, 1),
       (190, 'Saint-Martin (French part)', 'Saint-Martin', '663', 'MF', '590', 'Marigot', 'EUR', '€', '.mf', 'Americas',
        '🇲🇫', 'U+1F1F2 U+1F1EB', 1, 1, 1),
       (191, 'Samoa', 'Samoa', '882', 'WS', '685', 'Apia', 'WST', 'SAT', '.ws', 'Oceania', '🇼🇸', 'U+1F1FC U+1F1F8', 1,
        1, 1),
       (192, 'San Marino', 'San Marino', '674', 'SM', '378', 'San Marino', 'EUR', '€', '.sm', 'Europe', '🇸🇲',
        'U+1F1F8 U+1F1F2', 1, 1, 1),
       (193, 'Sao Tome and Principe', 'São Tomé e Príncipe', '678', 'ST', '239', 'Sao Tome', 'STD', 'Db', '.st',
        'Africa', '🇸🇹', 'U+1F1F8 U+1F1F9', 1, 1, 1),
       (194, 'Saudi Arabia', 'المملكة العربية السعودية', '682', 'SA', '966', 'Riyadh', 'SAR', '﷼', '.sa', 'Asia',
        '🇸🇦', 'U+1F1F8 U+1F1E6', 1, 1, 1),
       (195, 'Senegal', 'Sénégal', '686', 'SN', '221', 'Dakar', 'XOF', 'CFA', '.sn', 'Africa', '🇸🇳',
        'U+1F1F8 U+1F1F3', 1, 1, 1),
       (196, 'Serbia', 'Србија', '688', 'RS', '381', 'Belgrade', 'RSD', 'din', '.rs', 'Europe', '🇷🇸',
        'U+1F1F7 U+1F1F8', 1, 1, 1),
       (197, 'Seychelles', 'Seychelles', '690', 'SC', '248', 'Victoria', 'SCR', 'SRe', '.sc', 'Africa', '🇸🇨',
        'U+1F1F8 U+1F1E8', 1, 1, 1),
       (198, 'Sierra Leone', 'Sierra Leone', '694', 'SL', '232', 'Freetown', 'SLL', 'Le', '.sl', 'Africa', '🇸🇱',
        'U+1F1F8 U+1F1F1', 1, 1, 1),
       (199, 'Singapore', 'Singapore', '702', 'SG', '65', 'Singapur', 'SGD', '$', '.sg', 'Asia', '🇸🇬',
        'U+1F1F8 U+1F1EC', 1, 1, 1),
       (200, 'Slovakia', 'Slovensko', '703', 'SK', '421', 'Bratislava', 'EUR', '€', '.sk', 'Europe', '🇸🇰',
        'U+1F1F8 U+1F1F0', 1, 1, 1),
       (201, 'Slovenia', 'Slovenija', '705', 'SI', '386', 'Ljubljana', 'EUR', '€', '.si', 'Europe', '🇸🇮',
        'U+1F1F8 U+1F1EE', 1, 1, 1),
       (202, 'Solomon Islands', 'Solomon Islands', '090', 'SB', '677', 'Honiara', 'SBD', 'Si$', '.sb', 'Oceania',
        '🇸🇧', 'U+1F1F8 U+1F1E7', 1, 1, 1),
       (203, 'Somalia', 'Soomaaliya', '706', 'SO', '252', 'Mogadishu', 'SOS', 'Sh.so.', '.so', 'Africa', '🇸🇴',
        'U+1F1F8 U+1F1F4', 1, 1, 1),
       (204, 'South Africa', 'South Africa', '710', 'ZA', '27', 'Pretoria', 'ZAR', 'R', '.za', 'Africa', '🇿🇦',
        'U+1F1FF U+1F1E6', 1, 1, 1),
       (205, 'South Georgia', 'South Georgia', '239', 'GS', '500', 'Grytviken', 'GBP', '£', '.gs', 'Americas', '🇬🇸',
        'U+1F1EC U+1F1F8', 1, 1, 1),
       (206, 'South Sudan', 'South Sudan', '728', 'SS', '211', 'Juba', 'SSP', '£', '.ss', 'Africa', '🇸🇸',
        'U+1F1F8 U+1F1F8', 1, 1, 1),
       (207, 'Spain', 'España', '724', 'ES', '34', 'Madrid', 'EUR', '€', '.es', 'Europe', '🇪🇸', 'U+1F1EA U+1F1F8', 1,
        1, 1),
       (208, 'Sri Lanka', 'śrī laṃkāva', '144', 'LK', '94', 'Colombo', 'LKR', 'Rs', '.lk', 'Asia', '🇱🇰',
        'U+1F1F1 U+1F1F0', 1, 1, 1),
       (209, 'Sudan', 'السودان', '729', 'SD', '249', 'Khartoum', 'SDG', '.س.ج', '.sd', 'Africa', '🇸🇩',
        'U+1F1F8 U+1F1E9', 1, 1, 1),
       (210, 'Suriname', 'Suriname', '740', 'SR', '597', 'Paramaribo', 'SRD', '$', '.sr', 'Americas', '🇸🇷',
        'U+1F1F8 U+1F1F7', 1, 1, 1),
       (211, 'Svalbard And Jan Mayen Islands', 'Svalbard og Jan Mayen', '744', 'SJ', '47', 'Longyearbyen', 'NOK', 'kr',
        '.sj', 'Europe', '🇸🇯', 'U+1F1F8 U+1F1EF', 1, 1, 1),
       (212, 'Swaziland', 'Swaziland', '748', 'SZ', '268', 'Mbabane', 'SZL', 'E', '.sz', 'Africa', '🇸🇿',
        'U+1F1F8 U+1F1FF', 1, 1, 1),
       (213, 'Sweden', 'Sverige', '752', 'SE', '46', 'Stockholm', 'SEK', 'kr', '.se', 'Europe', '🇸🇪',
        'U+1F1F8 U+1F1EA', 1, 1, 1),
       (214, 'Switzerland', 'Schweiz', '756', 'CH', '41', 'Bern', 'CHF', 'CHf', '.ch', 'Europe', '🇨🇭',
        'U+1F1E8 U+1F1ED', 1, 1, 1),
       (215, 'Syria', 'سوريا', '760', 'SY', '963', 'Damascus', 'SYP', 'LS', '.sy', 'Asia', '🇸🇾', 'U+1F1F8 U+1F1FE', 1,
        1, 1),
       (216, 'Taiwan', '臺灣', '158', 'TW', '886', 'Taipei', 'TWD', '$', '.tw', 'Asia', '🇹🇼', 'U+1F1F9 U+1F1FC', 1, 1,
        1),
       (217, 'Tajikistan', 'Тоҷикистон', '762', 'TJ', '992', 'Dushanbe', 'TJS', 'SM', '.tj', 'Asia', '🇹🇯',
        'U+1F1F9 U+1F1EF', 1, 1, 1),
       (218, 'Tanzania', 'Tanzania', '834', 'TZ', '255', 'Dodoma', 'TZS', 'TSh', '.tz', 'Africa', '🇹🇿',
        'U+1F1F9 U+1F1FF', 1, 1, 1),
       (219, 'Thailand', 'ประเทศไทย', '764', 'TH', '66', 'Bangkok', 'THB', '฿', '.th', 'Asia', '🇹🇭',
        'U+1F1F9 U+1F1ED', 1, 1, 1),
       (220, 'Togo', 'Togo', '768', 'TG', '228', 'Lome', 'XOF', 'CFA', '.tg', 'Africa', '🇹🇬', 'U+1F1F9 U+1F1EC', 1, 1,
        1),
       (221, 'Tokelau', 'Tokelau', '772', 'TK', '690', '', 'NZD', '$', '.tk', 'Oceania', '🇹🇰', 'U+1F1F9 U+1F1F0', 1,
        1, 1),
       (222, 'Tonga', 'Tonga', '776', 'TO', '676', 'Nuku`alofa', 'TOP', '$', '.to', 'Oceania', '🇹🇴',
        'U+1F1F9 U+1F1F4', 1, 1, 1),
       (223, 'Trinidad And Tobago', 'Trinidad and Tobago', '780', 'TT', '+1-868', 'Port of Spain', 'TTD', '$', '.tt',
        'Americas', '🇹🇹', 'U+1F1F9 U+1F1F9', 1, 1, 1),
       (224, 'Tunisia', 'تونس', '788', 'TN', '216', 'Tunis', 'TND', 'ت.د', '.tn', 'Africa', '🇹🇳', 'U+1F1F9 U+1F1F3',
        1, 1, 1),
       (225, 'Turkey', 'Türkiye', '792', 'TR', '90', 'Ankara', 'TRY', '₺', '.tr', 'Asia', '🇹🇷', 'U+1F1F9 U+1F1F7', 1,
        1, 1),
       (226, 'Turkmenistan', 'Türkmenistan', '795', 'TM', '993', 'Ashgabat', 'TMT', 'T', '.tm', 'Asia', '🇹🇲',
        'U+1F1F9 U+1F1F2', 1, 1, 1),
       (227, 'Turks And Caicos Islands', 'Turks and Caicos Islands', '796', 'TC', '+1-649', 'Cockburn Town', 'USD', '$',
        '.tc', 'Americas', '🇹🇨', 'U+1F1F9 U+1F1E8', 1, 1, 1),
       (228, 'Tuvalu', 'Tuvalu', '798', 'TV', '688', 'Funafuti', 'AUD', '$', '.tv', 'Oceania', '🇹🇻',
        'U+1F1F9 U+1F1FB', 1, 1, 1),
       (229, 'Uganda', 'Uganda', '800', 'UG', '256', 'Kampala', 'UGX', 'USh', '.ug', 'Africa', '🇺🇬',
        'U+1F1FA U+1F1EC', 1, 1, 1),
       (230, 'Ukraine', 'Україна', '804', 'UA', '380', 'Kiev', 'UAH', '₴', '.ua', 'Europe', '🇺🇦', 'U+1F1FA U+1F1E6',
        1, 1, 1),
       (231, 'United Arab Emirates', 'دولة الإمارات العربية المتحدة', '784', 'AE', '971', 'Abu Dhabi', 'AED', 'إ.د',
        '.ae', 'Asia', '🇦🇪', 'U+1F1E6 U+1F1EA', 1, 1, 1),
       (232, 'United Kingdom', 'United Kingdom', '826', 'GB', '44', 'London', 'GBP', '£', '.uk', 'Europe', '🇬🇧',
        'U+1F1EC U+1F1E7', 1, 1, 1),
       (233, 'United States', 'United States', '840', 'US', '1', 'Washington', 'USD', '$', '.us', 'Americas', '🇺🇸',
        'U+1F1FA U+1F1F8', 1, 1, 1),
       (234, 'United States Minor Outlying Islands', 'United States Minor Outlying Islands', '581', 'UM', '1', '',
        'USD', '$', '.us', 'Americas', '🇺🇲', 'U+1F1FA U+1F1F2', 1, 1, 1),
       (235, 'Uruguay', 'Uruguay', '858', 'UY', '598', 'Montevideo', 'UYU', '$', '.uy', 'Americas', '🇺🇾',
        'U+1F1FA U+1F1FE', 1, 1, 1),
       (236, 'Uzbekistan', 'O‘zbekiston', '860', 'UZ', '998', 'Tashkent', 'UZS', 'лв', '.uz', 'Asia', '🇺🇿',
        'U+1F1FA U+1F1FF', 1, 1, 1),
       (237, 'Vanuatu', 'Vanuatu', '548', 'VU', '678', 'Port Vila', 'VUV', 'VT', '.vu', 'Oceania', '🇻🇺',
        'U+1F1FB U+1F1FA', 1, 1, 1),
       (238, 'Vatican City State (Holy See)', 'Vaticano', '336', 'VA', '379', 'Vatican City', 'EUR', '€', '.va',
        'Europe', '🇻🇦', 'U+1F1FB U+1F1E6', 1, 1, 1),
       (239, 'Venezuela', 'Venezuela', '862', 'VE', '58', 'Caracas', 'VEF', 'Bs', '.ve', 'Americas', '🇻🇪',
        'U+1F1FB U+1F1EA', 1, 1, 1),
       (240, 'Vietnam', 'Việt Nam', '704', 'VN', '84', 'Hanoi', 'VND', '₫', '.vn', 'Asia', '🇻🇳', 'U+1F1FB U+1F1F3', 1,
        1, 1),
       (241, 'Virgin Islands (British)', 'British Virgin Islands', '092', 'VG', '+1-284', 'Road Town', 'USD', '$',
        '.vg', 'Americas', '🇻🇬', 'U+1F1FB U+1F1EC', 1, 1, 1),
       (242, 'Virgin Islands (US)', 'United States Virgin Islands', '850', 'VI', '+1-340', 'Charlotte Amalie', 'USD',
        '$', '.vi', 'Americas', '🇻🇮', 'U+1F1FB U+1F1EE', 1, 1, 1),
       (243, 'Wallis And Futuna Islands', 'Wallis et Futuna', '876', 'WF', '681', 'Mata Utu', 'XPF', '₣', '.wf',
        'Oceania', '🇼🇫', 'U+1F1FC U+1F1EB', 1, 1, 1),
       (244, 'Western Sahara', 'الصحراء الغربية', '732', 'EH', '212', 'El-Aaiun', 'MAD', 'MAD', '.eh', 'Africa', '🇪🇭',
        'U+1F1EA U+1F1ED', 1, 1, 1),
       (245, 'Yemen', 'اليَمَن', '887', 'YE', '967', 'Sanaa', 'YER', '﷼', '.ye', 'Asia', '🇾🇪', 'U+1F1FE U+1F1EA', 1,
        1, 1),
       (246, 'Zambia', 'Zambia', '894', 'ZM', '260', 'Lusaka', 'ZMW', 'ZK', '.zm', 'Africa', '🇿🇲', 'U+1F1FF U+1F1F2',
        1, 1, 1),
       (247, 'Zimbabwe', 'Zimbabwe', '716', 'ZW', '263', 'Harare', 'ZWL', '$', '.zw', 'Africa', '🇿🇼',
        'U+1F1FF U+1F1FC', 1, 1, 1),
       (248, 'Kosovo', 'Republika e Kosovës', '926', 'XK', '383', 'Pristina', 'EUR', '€', '.xk', 'Europe', '🇽🇰',
        'U+1F1FD U+1F1F0', 1, 1, 1),
       (249, 'Curaçao', 'Curaçao', '531', 'CW', '599', 'Willemstad', 'ANG', 'ƒ', '.cw', 'Americas', '🇨🇼',
        'U+1F1E8 U+1F1FC', 1, 1, 1),
       (250, 'Sint Maarten (Dutch part)', 'Sint Maarten', '534', 'SX', '1721', 'Philipsburg', 'ANG', 'ƒ', '.sx',
        'Americas', '🇸🇽', 'U+1F1F8 U+1F1FD', 1, 1, 1);








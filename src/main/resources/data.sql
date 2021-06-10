INSERT INTO kommune(kommune_kode, navn) VALUES (101, 'København');
INSERT INTO `kommune`(`kommune_kode`, `navn`) VALUES (147, 'Frederiksberg');
INSERT INTO `kommune`(`kommune_kode`, `navn`) VALUES (151, 'Ballerup');
INSERT INTO `kommune`(`kommune_kode`, `navn`) VALUES (153, 'Brøndby');
INSERT INTO `kommune`(`kommune_kode`, `navn`) VALUES (155, 'Dragør');
INSERT INTO `kommune`(`kommune_kode`, `navn`) VALUES (157, 'Gentofte');

INSERT INTO sogn(sogn_kode, smitte_niveau, navn, kommune_kode, nedlukning) VALUES (1, 2, 'København Sogn', 101, '2021-06-10');
INSERT INTO sogn(sogn_kode, smitte_niveau, navn, kommune_kode) VALUES (2, 1, 'Absalon Sogn', 101);
INSERT INTO sogn(sogn_kode, smitte_niveau, navn, kommune_kode) VALUES (4, 5, 'Frederiksberg Sogn', 147);

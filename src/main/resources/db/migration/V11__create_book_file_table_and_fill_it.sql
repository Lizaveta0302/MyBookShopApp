drop table if exists shop.book_file;

create table shop.book_file
(
    id         bigserial not null,
    hash varchar(255),
    path  varchar(255),
    book_file_type_id int,
    book_id INT NOT NULL,
    CONSTRAINT book_file_fk
        FOREIGN KEY (book_id) REFERENCES shop.books (id),
    primary key (id)
);

insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (1, 'oWwgqobmXW', 'Inverse logistical task-force', 0,               1);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (2, 'ugqGju8GXYD', 'Configurable national knowledge base', 1,       2);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (3, 'NHY4IemTo1Pt', 'Progressive disintermediate contingency', 0,   3);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (4, '7kEIM4x42e', 'Universal homogeneous neural-net', 1,            4);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (5, 'q3PfwNFG', 'User-centric client-server database', 0,           5);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (6, '0LxQhwDYp8', 'Extended multi-state internet solution', 0,      6);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (7, 'apmR9imGPKt', 'Open-architected secondary access', 0,          7);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (8, 'ClDaFjIm', 'Synergized optimal website', 0,                    8);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (9, 'zWGAde8z9', 'Profound background toolset', 2,                  9);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (10, 'sMIU3chYW', 'Centralized explicit neural-net', 2,             14);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (11, 'Vimq28r', 'Object-based bifurcated data-warehouse', 0,        13);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (12, 'bqYbrEgH9Y', 'Robust foreground task-force', 0,               18);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (13, 'Zq39310zW1', 'Profound even-keeled toolset', 2,               16);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (14, 'Q4lazJvPLWF', 'Operative object-oriented orchestration', 0,   14);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (15, 'MicA0K5Xvr', 'Face to face client-driven framework', 2,       12);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (16, 'JCxyEDkqNm', 'Polarised tertiary frame', 2,                   8);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (17, 'LLiiXr', 'Public-key dynamic extranet', 0,                    9);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (18, '7zP9zD', 'Innovative bifurcated forecast', 2,                 10);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (19, 'HsUyK984', 'Upgradable zero tolerance local area network', 2, 17);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (20, 'xhVvdLX', 'Centralized motivating middleware', 1,             20);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (21, 'gEdCYpRDxnY', 'Cross-platform tangible monitoring', 1,        26);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (22, 'yag4EB9rItC', 'Visionary background hierarchy', 0,            24);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (23, 'y8RKHmc', 'Assimilated directional intranet', 2,              22);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (24, 'aqThP2yv', 'De-engineered zero defect initiative', 1,         23);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (25, 'PR7iPkXo', 'Balanced discrete adapter', 2,                    27);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (26, 'I6NptYSAS5', 'Quality-focused transitional initiative', 0,    25);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (27, 'w7IVktf', 'Optimized bandwidth-monitored groupware', 1,       29);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (28, 'f0u0BeOy4', 'Persevering zero administration database', 1,    2);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (29, 'dAa7kWs', 'Visionary 4th generation forecast', 2,             26);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (30, 'tOYGz2Lmq', 'Customizable heuristic forecast', 1,             36);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (31, 'eSqikPB', 'Right-sized directional contingency', 1,           3);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (32, 'iBurvFoo', 'Virtual composite monitoring', 1,                 38);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (33, 'ZELW4Tx8OAH', 'Intuitive coherent policy', 2,                 33);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (34, 'sbwhqGDGr82', 'Reduced cohesive framework', 1,                13);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (35, 'ANZEfo75qHd', 'Optional mobile strategy', 1,                  36);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (36, 'EfbTmKcBrG8', 'Robust fault-tolerant help-desk', 1,           30);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (37, 'Des2mk7okcT', 'Multi-tiered holistic standardization', 2,     37);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (38, 'h2ZiEWj58', 'Compatible context-sensitive', 0,                33);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (39, 'rUj189TZi8', 'Future-proofed cohesive collaboration', 1,      33);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (40, 'c0bXrvN', 'Synergistic 3rd generation instruction set', 2,    44);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (41, 'Q9WCuf3dlJiw', 'Cross-group interactive Graphic Interface', 0,4);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (42, 'TlVDpQ', 'Organic encompassing installation', 1,              48);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (43, 'QtH8W3Ve', 'Progressive incremental synergy', 2,              40);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (44, 'wo1FWPYjTzFi', 'Front-line coherent superstructure', 0,       43);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (45, 'VVGAYWqF', 'Organic transitional monitoring', 0,              44);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (46, 'Ds0iUNE', 'Balanced responsive service-desk', 0,              4);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (47, 'RtxZ7ohv', 'Optional 5th generation collaboration', 0,        40);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (48, 'FDUa34la8tP', 'Profit-focused intermediate portal', 0,        46);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (49, 'onaRrlWHLwD', 'Stand-alone bottom-line projection', 2,        44);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (50, 'L0Hl1phg', 'Profound value-added interface', 1,               57);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (51, 'bKeh9sKHLaY', 'Optimized encompassing middleware', 0,         54);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (52, 'npcOHLTsV', 'Public-key global instruction set', 1,           53);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (53, '5Q1aqbs', 'User-centric multimedia leverage', 1,              53);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (54, 'zyfHOnf', 'Phased client-server monitoring', 2,               52);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (55, 'e6NaxmQ57DS', 'Adaptive holistic migration', 0,               51);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (56, '394CB3LQ9zk', 'Advanced reciprocal help-desk', 2,             5);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (57, 'Tvk6ZXp3', 'Automated exuding focus group', 1,                50);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (58, 'JLLCnPBs', 'Customizable responsive forecast', 1,             57);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (59, 'Lpc9s6dL', 'Pre-emptive explicit groupware', 0,               5);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (60, 'w34SVFKodJZ', 'Decentralized content-based knowledge user', 0,65);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (61, 'cWj9FMkr', 'Right-sized systematic database', 1,              6);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (62, 'FzW8nqlD54', 'Assimilated scalable interface', 1,             64);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (63, 'if7a3pnNxd4e', 'Streamlined homogeneous extranet', 2,         63);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (64, 'Q7nct7MSY', 'Grass-roots didactic encryption', 2,             66);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (65, 'aEC55vKD', 'Total coherent throughput', 1,                    63);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (66, 'yZe2VcPm6', 'Synergistic optimal capacity', 1,                62);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (67, 'wY0pwg82L3', 'Re-engineered exuding migration', 0,            6);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (68, 'cK9RZJLOmXG', 'Re-contextualized ', 2,                        16);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (69, 'oLntJbdQq3', 'Compatible composite customer loyalty', 1,      6);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (70, 'dn30jwxc', 'Future-proofed intangible encoding', 0,           76);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (71, 'ZLNMfLgZ', 'Business-focused transitional capacity', 1,       72);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (72, 'YqTWMHK', 'Synergistic contextually-based migration', 0,      71);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (73, 'zYolYmnyLn', 'Profit-focused eco-centric superstructure', 2,  76);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (74, 'DS6YD2Aw9', 'Persevering neutral help-desk', 1,               79);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (75, 'cyJpaLbro7H', 'Enterprise-wide even-keeled internet', 0,      76);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (76, 'M8mM03foR', 'Face to face homogeneous portal', 1,             74);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (77, 'EsCyrgbo', 'Synergistic zero tolerance flexibility', 2,       74);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (78, 'rxZC6q6mN0', 'Cross-platform neutral extranet', 0,            77);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (79, 'gJ7kKZQ', 'Organized explicit groupware', 0,                  73);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (80, 'fNWwINd', 'Multi-lateral non-volatile definition', 2,         82);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (81, 'hWG8dKLxWACV', 'Persistent client-driven model', 2,           8);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (82, 'F09PExS', 'User-centric demand-driven orchestration', 1,      87);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (83, 'fE1luiAitdxm', 'Assimilated zero tolerance firmware', 0,      85);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (84, '4Cgg3cAgZx', 'Balanced fresh-thinking orchestration', 2,      87);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (85, 'lv0q1pP7qn', 'Open-source logistical framework', 0,           8);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (86, 's0Kc2Zysk3hV', 'Devolved systematic orchestration', 0,        84);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (87, 'cRtggOjL8hG', 'Compatible 24/7 paradigm', 0,                  88);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (88, 'btCw9O', 'Centralized dynamic initiative', 2,                 89);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (89, 'Be9V6lwXFL', 'Versatile composite paradigm', 1,               80);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (90, 'Bs32YB9shY11', 'Fundamental stable attitude', 1,              99);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (91, '5g8CT6kY', 'User-centric eco-centric local area network', 1,  90);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (92, 'LKOQYV', 'Cloned modular attitude', 1,                        95);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (93, '72wNivt3u', 'Networked impactful capability', 1,              92);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (94, 'qe37YXwZpZk', 'Advanced asymmetric service-desk', 2,          94);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (95, 'WkR1oCK0', 'Implemented background service-desk', 1,          93);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (96, 'idt7G8', 'Synergized secondary solution', 1,                  29);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (97, 'U3EV7Or', 'Versatile asymmetric hub', 0,                      19);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (98, 'E1ABa1SrOLH', 'Up-sized clear-thinking data-warehouse', 2,    94);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (99, 'GlW66yvK', 'Face to face web-enabled matrices', 1,            93);
insert into shop.book_file (id, hash, path, book_file_type_id, book_id) values (100, 'IeTzwW7i', 'Versatile zero defect synergy', 1,               1);


use std::collections::{HashMap, HashSet};

fn solve(input: &str, ignore_depth: bool) -> i64 {
    let mut coords = HashSet::new();
    let mut unparsed_portals = Vec::new();

    for (y, curr_line) in input.lines().enumerate() {
        for (x, curr_char) in curr_line.chars().enumerate() {
            match curr_char {
                ' ' => {}
                '#' => {}
                '.' => {
                    coords.insert((x as i64, y as i64));
                }
                'A'..='Z' => {
                    unparsed_portals.push(((x as i64, y as i64), curr_char));
                }
                _ => unreachable!()
            };
        }
    }


    let mut parsed_portals_str: HashMap<String, Vec<((i64, i64), bool)>> = HashMap::new();
    let mut parsed_portals_coord = HashMap::new();

    while !unparsed_portals.is_empty() {
        let portal = unparsed_portals.first().unwrap().clone();

        let neighbours = get_neighbours(portal.0, &coords);
        let is_left_or_up = neighbours.len() == 0;

        let coords_left = coords.iter().filter(|c| c.0 < (portal.0).0).collect::<Vec<_>>().len();
        let coords_right = coords.iter().filter(|c| c.0 > (portal.0).0).collect::<Vec<_>>().len();
        let coords_top = coords.iter().filter(|c| c.1 > (portal.0).1).collect::<Vec<_>>().len();
        let coords_bottom = coords.iter().filter(|c| c.1 < (portal.0).1).collect::<Vec<_>>().len();

        let is_inner = coords_left > 0 && coords_right > 0 && coords_top > 0 && coords_bottom > 0;

        let portal_coordinates = &unparsed_portals.iter().map(|e| e.0).collect();

        let portal_neighbour = *get_neighbours(portal.0, portal_coordinates).iter().next().unwrap();

        let portal_neighbour_index = unparsed_portals.iter().position(|e| e.0 == portal_neighbour).unwrap();
        let portal_neighbour_char = unparsed_portals[portal_neighbour_index].1;

        let mut name = String::new();
        name.push(portal.1);
        name.push(portal_neighbour_char);

        let end_coords = if is_left_or_up {
            portal_neighbour
        } else {
            portal.0
        };

        if parsed_portals_str.contains_key(&name) {
            parsed_portals_str.get_mut(&name).unwrap().push((end_coords, is_inner));
            parsed_portals_coord.insert(end_coords, (name, is_inner));
        } else {
            parsed_portals_str.insert(name.clone(), vec![(end_coords, is_inner)]);
            parsed_portals_coord.insert(end_coords, (name, is_inner));
        }

        unparsed_portals.remove(portal_neighbour_index);
        unparsed_portals.remove(0);
    }

    let parsed_portal_coordinates = &parsed_portals_coord.keys().map(|e| *e).collect();

    let mut visited = HashSet::new();

    let mut to_visit: HashSet<(i64, i64, u64)> = HashSet::new();
    let vec1 = &parsed_portals_str[&"AA".to_string()];
    let zero_neighbours = get_neighbours(vec1[0].0, &coords).iter().map(|e| (e.0, e.1, 0)).collect::<HashSet<_>>();

    to_visit.extend(&zero_neighbours);
    visited.extend(&zero_neighbours);
    visited.insert(((vec1[0].0).0, (vec1[0].0).1, 0));

    let mut distance = 0;
    while !to_visit.is_empty() {
        let mut new_coordinates: HashSet<(i64, i64, u64)> = HashSet::new();

        for pos in &to_visit {
            let curr_depth = pos.2;
            let curr_coord = (pos.0, pos.1);

            for neighbour in get_neighbours(curr_coord, &coords) {
                if !visited.contains(&(neighbour.0, neighbour.1, curr_depth)) {
                    visited.insert((neighbour.0, neighbour.1, curr_depth));
                    new_coordinates.insert((neighbour.0, neighbour.1, curr_depth));
                }
            }

            for neighbour in get_neighbours(curr_coord, &parsed_portal_coordinates) {
                if !visited.contains(&(neighbour.0, neighbour.1, curr_depth)) {
                    let portal_value = &parsed_portals_coord[&neighbour];

                    if portal_value.0 == "ZZ".to_string() && (curr_depth == 0 || ignore_depth) {
                        return distance;
                    }

                    let new_depth = if portal_value.1 {
                        if ignore_depth == false {
                            curr_depth + 1
                        } else {
                            0
                        }
                    } else {
                        if !ignore_depth {
                            if curr_depth == 0 {
                                continue;
                            }
                            curr_depth - 1
                        } else {
                            0
                        }
                    };

                    let portal_coords = &parsed_portals_str[&portal_value.0];
                    if curr_depth > 0 && (portal_value.0 == "AA".to_string() || portal_value.0 == "ZZ".to_string()) {
                        continue;
                    }

                    if portal_coords[0].0 == neighbour {
                        let portal_coord = portal_coords[1].0;
                        new_coordinates.extend(get_neighbours(portal_coord, &coords).iter().map(|e| (e.0, e.1, new_depth)).collect::<HashSet<_>>());
                        visited.insert((portal_coord.0, portal_coord.1, new_depth));
                    } else {
                        let portal_coord = portal_coords[0].0;
                        new_coordinates.extend(get_neighbours(portal_coord, &coords).iter().map(|e| (e.0, e.1, new_depth)).collect::<HashSet<_>>());
                        visited.insert((portal_coord.0, portal_coord.1, new_depth));
                    }

                    visited.insert((neighbour.0, neighbour.1, new_depth));
                }
            }
        }

        distance += 1;

        to_visit = new_coordinates;
    }

    unreachable!()
}

fn get_neighbours<'a>(coord: (i64, i64), coords: &HashSet<(i64, i64)>) -> HashSet<(i64, i64)> {
    let mut neighbours = HashSet::new();

    neighbours.insert(coords.get(&(coord.0, coord.1 + 1)));
    neighbours.insert(coords.get(&(coord.0 + 1, coord.1)));
    neighbours.insert(coords.get(&(coord.0, coord.1 - 1)));
    neighbours.insert(coords.get(&(coord.0 - 1, coord.1)));

    neighbours.iter().flatten().map(|e| **e).collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let i = solve(include_str!("day20"), true);
        println!("{}", i);
        assert_eq!(i, 400);
    }

    #[test]
    fn p2() {
        let i = solve(include_str!("day20"), false);
        println!("{}", i);
        assert_eq!(i, 4986);
    }

    #[test]
    fn test1() {
        let input = "         A
         A
  #######.#########
  #######.........#
  #######.#######.#
  #######.#######.#
  #######.#######.#
  #####  B    ###.#
BC...##  C    ###.#
  ##.##       ###.#
  ##...DE  F  ###.#
  #####    G  ###.#
  #########.#####.#
DE..#######...###.#
  #.#########.###.#
FG..#########.....#
  ###########.#####
             Z
             Z
";

        let i = solve(input, true);
        println!("{}", i);
        assert_eq!(i, 23);
    }

    #[test]
    fn test2() {
        let input = "                   A
                   A
  #################.#############
  #.#...#...................#.#.#
  #.#.#.###.###.###.#########.#.#
  #.#.#.......#...#.....#.#.#...#
  #.#########.###.#####.#.#.###.#
  #.............#.#.....#.......#
  ###.###########.###.#####.#.#.#
  #.....#        A   C    #.#.#.#
  #######        S   P    #####.#
  #.#...#                 #......VT
  #.#.#.#                 #.#####
  #...#.#               YN....#.#
  #.###.#                 #####.#
DI....#.#                 #.....#
  #####.#                 #.###.#
ZZ......#               QG....#..AS
  ###.###                 #######
JO..#.#.#                 #.....#
  #.#.#.#                 ###.#.#
  #...#..DI             BU....#..LF
  #####.#                 #.#####
YN......#               VT..#....QG
  #.###.#                 #.###.#
  #.#...#                 #.....#
  ###.###    J L     J    #.#.###
  #.....#    O F     P    #.#...#
  #.###.#####.#.#####.#####.###.#
  #...#.#.#...#.....#.....#.#...#
  #.#####.###.###.#.#.#########.#
  #...#.#.....#...#.#.#.#.....#.#
  #.###.#####.###.###.#.#.#######
  #.#.........#...#.............#
  #########.###.###.#############
           B   J   C
           U   P   P               ";

        let i = solve(input, true);
        println!("{}", i);
        assert_eq!(i, 58);
    }

    #[test]
    fn test3() {
        let input = "         A
         A
  #######.#########
  #######.........#
  #######.#######.#
  #######.#######.#
  #######.#######.#
  #####  B    ###.#
BC...##  C    ###.#
  ##.##       ###.#
  ##...DE  F  ###.#
  #####    G  ###.#
  #########.#####.#
DE..#######...###.#
  #.#########.###.#
FG..#########.....#
  ###########.#####
             Z
             Z
";

        let i = solve(input, false);
        println!("{}", i);
        assert_eq!(i, 26);
    }
}

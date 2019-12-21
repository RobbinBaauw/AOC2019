use std::collections::{HashMap, HashSet};
use std::collections::VecDeque;
use std::iter::FromIterator;
use std::cmp::min;
use std::borrow::BorrowMut;

fn part1(input: &str) -> u32 {

    struct Env {
        maze: Maze,
        dependencies: HashMap<char, Vec<char>>,
        cache: HashMap<String, u32>
    }

    fn solve(curr_char: char, to_do_keys: Vec<&char>, env: &mut Env) -> u32 {
        if to_do_keys.is_empty() {
            return 0;
        }

        let mut cache_key = String::new();
        cache_key.push(curr_char);
        cache_key.push_str(String::from_iter(to_do_keys.iter().map(|e| *e).clone()).as_str());

        if !env.cache.contains_key(&cache_key) {
            let mut result = std::u32::MAX;
            for (i, key) in to_do_keys.iter().enumerate() {
                let left = env.dependencies[key].iter().collect::<HashSet<_>>();
                let right = to_do_keys.iter().map(|e| *e).collect::<HashSet<_>>();
                if left.intersection(&right).collect::<HashSet<_>>().len() == 0 {
                    let mut left_over_keys = to_do_keys.clone();
                    left_over_keys.remove(i);

                    let dist_to_key = env.maze.distance(curr_char, **key);
                    let child_distances = solve(**key, left_over_keys, env);
                    let curr_dist = dist_to_key + child_distances;
                    result = min(curr_dist, result);
                }
            }
            env.cache.insert(cache_key.clone(), result);
        }

        env.cache[&cache_key]
    };

    let maze = parse_input(input);
    let dependencies = generate_dependencies(&maze);
    let cache: HashMap<String, u32> = HashMap::new();

    let map = dependencies.clone();
    let keys = map.keys().collect();

    let mut env = Env {
        maze,
        dependencies,
        cache
    };

    solve('@', keys, env.borrow_mut())
}

fn generate_dependencies(maze: &Maze) -> HashMap<char, Vec<char>> {

    let mut result: HashMap<char, Vec<char>> = HashMap::new();

    let mut queue: VecDeque<(Location, Vec<char>)> = VecDeque::new();
    let mut seen: HashSet<Location> = HashSet::new();

    let start = maze.starting_location;
    queue.push_back((start, vec![]));

    while !queue.is_empty() {
        let curr_value = queue.pop_front().unwrap();
        let curr_pos = curr_value.0;

        let neighbours = maze.get_neighbours(curr_pos);
        for neighbour in neighbours {
            let neighbour_char = maze.grid[&neighbour];

            if seen.contains(&neighbour) || neighbour_char == '#' {
                continue;
            }

            seen.insert(neighbour);

            let mut curr_dependencies = curr_value.1.iter().clone().map(|e| *e).collect::<Vec<_>>();

            if ('a'..'z').contains(&neighbour_char) {
                result.insert(neighbour_char, curr_value.1.iter().map(|e| *e).collect::<Vec<_>>());
                curr_dependencies.push(neighbour_char);
            }

            if ('A'..'Z').contains(&neighbour_char) {
                curr_dependencies.push(neighbour_char.to_ascii_lowercase());
            }

            queue.push_back((neighbour, curr_dependencies));
        }
    }

    result
}

fn parse_input(input: &str) -> Maze {

    let mut starting_location = (0, 0);
    let mut grid = HashMap::new();

    for (y, curr_line) in input.lines().enumerate() {
        for (x, curr_char) in curr_line.chars().enumerate() {
            match curr_char {
                '.' => {
                    grid.insert((x as i64, y as i64), curr_char);
                }
                '#' => {}
                '@' => {
                    starting_location = (x as i64, y as i64);
                    grid.insert((x as i64, y as i64), curr_char);
                }
                'a'..='z' => {
                    grid.insert((x as i64, y as i64), curr_char);
                }
                'A'..='Z' => {
                    grid.insert((x as i64, y as i64), curr_char);
                }
                _ => unreachable!()
            };
        }
    }

    Maze {
        starting_location,
        grid,
        distance_cache: HashMap::new()
    }
}

type Location = (i64, i64);
type Letters = HashMap<Location, char>;

struct Maze {
    starting_location: Location,
    grid: Letters,
    distance_cache: HashMap<(char, char), u32>
}

impl Maze {
    fn distance(&mut self, loc_a: char, loc_b: char) -> u32 {
        let key = (loc_a, loc_b);
        if !self.distance_cache.contains_key(&key) {
            self.distance_cache.insert(key, self.distance_compute(loc_a, loc_b));
        }

        self.distance_cache[&key]
    }

    fn distance_compute(&self, loc_a: char, loc_b: char) -> u32 {
        if loc_a == loc_b {
            return 0;
        }

        let mut visited: HashSet<Location> = HashSet::new();
        let mut to_visit: HashSet<Location> = HashSet::new();

        let start = self.grid.iter().find(|e| *e.1 == loc_a).unwrap().0;

        visited.insert(*start);
        to_visit.insert(*start);

        let mut distance = 0;

        while !to_visit.is_empty() {

            let mut new_coordinates: HashSet<(i64, i64)> = HashSet::new();

            distance += 1;

            for pos in &to_visit {
                let neighbours = self.get_neighbours(*pos);
                for neighbour in neighbours {
                    let neighbour_char = self.grid[&neighbour];
                    if visited.contains(&neighbour) {
                        continue;
                    }

                    visited.insert(neighbour);

                    if neighbour_char == loc_b {
                        return distance;
                    } else {
                        new_coordinates.insert(neighbour);
                    }
                }
            }

            to_visit = new_coordinates;
        }

        unreachable!()
    }


    fn get_neighbours(&self, pos: Location) -> HashSet<Location> {
        let mut neighbours = HashSet::new();

        if self.grid.contains_key(&(pos.0 + 1, pos.1)) {
            neighbours.insert((pos.0 + 1, pos.1));
        }
        if self.grid.contains_key(&(pos.0 - 1, pos.1)) {
            neighbours.insert((pos.0 - 1, pos.1));
        }
        if self.grid.contains_key(&(pos.0, pos.1 + 1)) {
            neighbours.insert((pos.0, pos.1 + 1));
        }
        if self.grid.contains_key(&(pos.0, pos.1 - 1)) {
            neighbours.insert((pos.0, pos.1 - 1));
        }

        neighbours

    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let i = part1(include_str!("day18"));
        assert_eq!(i, 400);
    }

//    #[test]
//    fn p2() {
//        let i = part1(include_str!("day18"));
//        assert_eq!(i, 4986);
//    }

    #[test]
    fn test1() {
        let i = part1("#########
#b.A.@.a#
#########");
        assert_eq!(i, 8);
    }

    #[test]
    fn test2() {
        let i = part1("########################
#f.D.E.e.C.b.A.@.a.B.c.#
######################.#
#d.....................#
########################
");
        assert_eq!(i, 86);
    }

    #[test]
    fn test3() {
        let i = part1("########################
#@..............ac.GI.b#
###d#e#f################
###A#B#C################
###g#h#i################
########################");
        assert_eq!(i, 81);
    }

    #[test]
    fn test4() {
        let i = part1("#################
#i.G..c...e..H.p#
########.########
#j.A..b...f..D.o#
########@########
#k.E..a...g..B.n#
########.########
#l.F..d...h..C.m#
#################");
        assert_eq!(i, 136);
    }

    #[test]
    fn test5() {
        let i = part1("########################
#...............b.C.D.f#
#.######################
#.....@.a.B.c.d.A.e.F.g#
########################
");
        assert_eq!(i, 132);
    }
}

use std::collections::{HashSet, HashMap};
use arrayvec::ArrayVec;

pub fn part1(grid: [[bool; 5]; 5]) -> usize {

    let mut previous_grids = HashSet::new();
    previous_grids.insert(grid);

    let mut curr_grid = grid;
    loop {
        let new_grid = get_new_grid(&curr_grid);
        if previous_grids.contains(&new_grid) {
            let mut sum = 0;
            for y in 0..5 {
                for x in 0..5 {
                    if new_grid[y][x] {
                        sum += 2_usize.pow((y * 5 + x) as u32);
                    }
                }
            }

            return sum;
        } else {
            previous_grids.insert(new_grid);
            curr_grid = new_grid;
        }
    }

}

pub fn part2(grid: [[bool; 5]; 5]) -> i32 {
    let mut start_grids = HashMap::new();

    start_grids.insert(0, grid);

    for layer in -200..200 {
        if layer != 0 {
            let new_layer = [[false; 5]; 5];
            start_grids.insert(layer, new_layer);
        }
    }

    let mut curr_grids = start_grids;
    for _ in 0..200 {
        let mut new_grids = HashMap::new();
        for layer in -200..200 {
            new_grids.insert(layer, get_new_grid_rec(layer, &curr_grids));
        }
        curr_grids = new_grids;
    }

    let mut total_bugs = 0;
    for layer in -200..200 {
        let curr_layer = curr_grids[&layer];
        total_bugs += curr_layer.iter().flatten().map(|f| *f as i32).sum::<i32>();
    }
    total_bugs
}

fn get_new_grid_rec(layer: i32, grids: &HashMap<i32, [[bool; 5]; 5]>) -> [[bool; 5]; 5] {
    let mut new_grid = [[false; 5]; 5];

    let grid = grids[&layer];
    for y in 0..5 {
        for x in 0..5 {
            if y == 2 && x == 2 {
                continue;
            }

            let is_bug = grid[y][x];

            let mut amount_of_bugs = 0;

            if y + 1 < 5 { amount_of_bugs += grid[y + 1][x] as i32 };
            if y > 0 { amount_of_bugs += grid[y - 1][x] as i32 };
            if x > 0 { amount_of_bugs += grid[y][x - 1] as i32 };
            if x + 1 < 5 { amount_of_bugs += grid[y][x + 1] as i32 };

            if x == 0 {
                let upper_layer_opt = grids.get(&(layer - 1));
                if let Some(upper_layer) = upper_layer_opt {
                    amount_of_bugs += upper_layer[2][1] as i32;
                }
            }

            if x == 4 {
                let upper_layer_opt = grids.get(&(layer - 1));
                if let Some(upper_layer) = upper_layer_opt {
                    amount_of_bugs += upper_layer[2][3] as i32;
                }
            }

            if y == 0 {
                let upper_layer_opt = grids.get(&(layer - 1));
                if let Some(upper_layer) = upper_layer_opt {
                    amount_of_bugs += upper_layer[1][2] as i32;
                }
            }

            if y == 4 {
                let upper_layer_opt = grids.get(&(layer - 1));
                if let Some(upper_layer) = upper_layer_opt {
                    amount_of_bugs += upper_layer[3][2] as i32;
                }
            }

            if x == 1 && y == 2 {
                let lower_layer_opt = grids.get(&(layer + 1));
                if let Some(lower_layer) = lower_layer_opt {
                    amount_of_bugs += lower_layer[0][0] as i32;
                    amount_of_bugs += lower_layer[1][0] as i32;
                    amount_of_bugs += lower_layer[2][0] as i32;
                    amount_of_bugs += lower_layer[3][0] as i32;
                    amount_of_bugs += lower_layer[4][0] as i32;
                }
            }

            if x == 3 && y == 2 {
                let lower_layer_opt = grids.get(&(layer + 1));
                if let Some(lower_layer) = lower_layer_opt {
                    amount_of_bugs += lower_layer[0][4] as i32;
                    amount_of_bugs += lower_layer[1][4] as i32;
                    amount_of_bugs += lower_layer[2][4] as i32;
                    amount_of_bugs += lower_layer[3][4] as i32;
                    amount_of_bugs += lower_layer[4][4] as i32;
                }
            }

            if x == 2 && y == 1 {
                let lower_layer_opt = grids.get(&(layer + 1));
                if let Some(lower_layer) = lower_layer_opt {
                    amount_of_bugs += lower_layer[0][0] as i32;
                    amount_of_bugs += lower_layer[0][1] as i32;
                    amount_of_bugs += lower_layer[0][2] as i32;
                    amount_of_bugs += lower_layer[0][3] as i32;
                    amount_of_bugs += lower_layer[0][4] as i32;
                }
            }

            if x == 2 && y == 3 {
                let lower_layer_opt = grids.get(&(layer + 1));
                if let Some(lower_layer) = lower_layer_opt {
                    amount_of_bugs += lower_layer[4][0] as i32;
                    amount_of_bugs += lower_layer[4][1] as i32;
                    amount_of_bugs += lower_layer[4][2] as i32;
                    amount_of_bugs += lower_layer[4][3] as i32;
                    amount_of_bugs += lower_layer[4][4] as i32;
                }
            }

            // is bug
            if is_bug {
                new_grid[y][x] = amount_of_bugs == 1;
            } else {
                new_grid[y][x] = amount_of_bugs == 1 || amount_of_bugs == 2;
            }
        }
    }

    new_grid
}


fn get_new_grid(grid: &[[bool; 5]; 5]) -> [[bool; 5]; 5] {
    let mut new_grid = [[false; 5]; 5];

    for y in 0..5 {
        for x in 0..5 {
            let is_bug = grid[y][x];

            let mut amount_of_bugs = 0;

            if y + 1 < 5 { amount_of_bugs += grid[y + 1][x] as i32 };
            if y > 0 { amount_of_bugs += grid[y - 1][x] as i32 };
            if x > 0 { amount_of_bugs += grid[y][x - 1] as i32 };
            if x + 1 < 5 { amount_of_bugs += grid[y][x + 1] as i32 };

            // is bug
            if is_bug {
                new_grid[y][x] = amount_of_bugs == 1;
            } else {
                new_grid[y][x] = amount_of_bugs == 1 || amount_of_bugs == 2;
            }
        }
    }

    new_grid
}

fn parse_input(input: &str) -> [[bool; 5]; 5] {
    input
        .trim()
        .lines()
        .map(|l| {
            let x: ArrayVec<_> = l.chars().map(|c| c == '#').collect();
            x.into_inner().unwrap()
        })
        .collect::<ArrayVec<_>>()
        .into_inner()
        .unwrap()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let input = parse_input(include_str!("day24"));
        let i = part1(input);
        assert_eq!(i, 3186366);
    }

    #[test]
    fn p2() {
        let input = parse_input(include_str!("day24"));
        let i = part2(input);
        assert_eq!(i, 2031);
    }

    #[test]
    fn test1() {
        let input = parse_input("....#
#..#.
#..##
..#..
#....");
        let i = part2(input);
        assert_eq!(i, 99);
    }

}

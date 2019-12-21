use std::f64::consts::PI;
use std::cmp::Ordering::Equal;

pub fn part1(pixels: &[(isize, isize)]) -> usize {

    let mut max_asteroids = 0;

    for pixel in pixels {
        let mut curr_hits = 0;

        'outer: for pixel2 in pixels {
            if pixel == pixel2 {
                continue;
            }

            for pixel3 in pixels {
                if pixel != pixel3 && pixel2 != pixel3 && is_in_line(pixel, pixel2, pixel3) {
                    continue 'outer;
                }
            }

            curr_hits += 1;
        }

        if curr_hits > max_asteroids {
            max_asteroids = curr_hits;
        }
    }

    max_asteroids
}

pub fn part2(pixels: &mut Vec<(isize, isize)>, start_pixel: (isize, isize)) -> Option<isize> {

    let mut angles: Vec<(f64, isize, (isize, isize))> = vec![];
    for (_i, pixel) in pixels.iter().enumerate() {
        if pixel == &start_pixel {
            continue;
        }

        let mut curr_angle = ((start_pixel.1 - pixel.1) as f64).atan2((start_pixel.0 - pixel.0) as f64);
        curr_angle += 2.0 * PI;
        curr_angle %= 2.0 * PI;

        let curr_dist = (start_pixel.1 - pixel.1).abs() + (start_pixel.0 - pixel.0).abs();

        angles.push((curr_angle, curr_dist, *pixel));
    }

    angles.sort_by(|a, b| {
        let angle_order = a.0.partial_cmp(&b.0).unwrap();
        if angle_order == Equal {
            a.1.partial_cmp(&b.1).unwrap()
        } else {
            angle_order
        }
    });

    let mut vaporized = 0;
    let mut angle_index = 0;

    for (i, angle) in angles.iter().enumerate() {
        if angle.0 >= 0.5 * PI {
            angle_index = i;
            break;
        }
    };

    while vaporized < 200 {

        let curr_pixel = angles.get(angle_index);
        let curr_angle = curr_pixel?.0;

        if vaporized == 199 {
            return Some((curr_pixel?.2).0 * 100 + (curr_pixel?.2).1);
        }

        vaporized += 1;

        angles.remove(angle_index);
        angle_index %= angles.len();

        loop {
            let next_angle = angles.get(angle_index)?.0;

            if (curr_angle - next_angle).abs() > std::f64::EPSILON {
                break;
            }

            angle_index += 1;
            angle_index %= angles.len();
        }
    }

    None
}

fn is_in_line(a: &(isize, isize), b: &(isize, isize), target: &(isize, isize)) -> bool {
    let a1 = (a.0 as f64, a.1 as f64);
    let b1 = (b.0 as f64, b.1 as f64);
    let target1 = (target.0 as f64, target.1 as f64);

    is_in_line_f64(&a1, &b1, &target1)
}

fn is_in_line_f64(a: &(f64, f64), b: &(f64, f64), target: &(f64, f64)) -> bool {

    let dist_1 = dist(a, target);
    let dist_2 = dist(target, b);

    let dist_full = dist(a, b);

    dist_1 + dist_2 - dist_full < 0.00000001
}

fn dist(a: &(f64, f64), b: &(f64, f64)) -> f64 {
    (((a.0 - b.0).powf(2.0) + (a.1 - b.1).powf(2.0)) as f64).sqrt()
}

fn parse_input(string: &str) -> Vec<(isize, isize)> {

    let lines = string
        .trim()
        .lines()
        .collect::<Vec<_>>();


    let mut result: Vec<(isize, isize)> = vec![];

    for (i, line) in lines.iter().enumerate() {
        for (j, x) in line.chars().enumerate() {
            if x == '#' {
                result.push((j as isize, i as isize));
            }
        }
    }

    result
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let input = parse_input(include_str!("day10"));
        let i = part1(&input);
        assert_eq!(i, 221);
    }

    #[test]
    fn p2() {
        let mut input = parse_input(include_str!("day10"));
        let i = part2(input.as_mut(), (11, 11));
        assert_eq!(i, Some(806));
    }
}

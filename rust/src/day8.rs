pub fn part1(pixels: &[[[u32; 6]; 25]]) -> usize {
    let min_pixels = pixels
        .iter()
        .map(|it| it.iter().flatten())
        .min_by_key(|it| it.clone().filter(|n| n == &&0).count()).expect("test");

    let one_digits = min_pixels.clone().filter(|it| it == &&1).count();
    let two_digits = min_pixels.filter(|it| it == &&2).count();

    one_digits * two_digits
}

pub fn part2(pixels: &[[[u32; 6]; 25]]) -> String {

    let mut output = String::new();

    for y in 0..5 {
        for x in 0..24 {
            for pixel in pixels {
                let i = pixel[x][y];
                match i {
                    0 => {
                        output.push(' ');
                        break;
                    },
                    1 => {
                        output.push('█');
                        break;
                    },
                    _ => {}
                }
            }
        }
        output.push('\n');
    }

    output
}

fn parse_input(string: &str) -> Vec<[[u32; 6]; 25]> {

    let mut layer_xy = vec![[[0; 6]; 25]];

    let mut curr_x = 0;
    let mut curr_y = 0;
    let mut curr_layer = 0;

    for (_i, c) in string.trim().chars().enumerate() {
        layer_xy.get_mut(curr_layer).unwrap()[curr_x][curr_y] = c.to_digit(10).expect("Oof");

        curr_x += 1;

        if curr_x == 25 {
            curr_x = 0;

            curr_y += 1;

            if curr_y == 6 {
                curr_layer += 1;
                layer_xy.push([[0; 6]; 25]);
                curr_y = 0;
            }
        }
    }

    layer_xy
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let input = parse_input(include_str!("day8"));
        let i = part1(&input);
        println!("{}", i);
        assert_eq!(i, 1950);
    }

    #[test]
    fn p2() {
        let input = parse_input(include_str!("day8"));
        let i = part2(&input);
        println!("{}", i);
        assert_eq!(i, "fd");
    }

    #[test]
    fn test1() {
        let input = parse_input("123456789012");
        let i = part1(&input);
        assert_eq!(i, 1);
    }

}

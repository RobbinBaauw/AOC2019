use std::collections::HashMap;

struct CreatedElement {
    output_name: String,
    output_amount: isize,
    input_elements: Vec<(isize, String)>
}

fn part1(elements: &HashMap<&str, CreatedElement>) -> isize {
    let mut root_elements: HashMap<&str, isize> = HashMap::new();
    get_input_elements(elements, &mut root_elements, elements.get("FUEL").unwrap(), 1);
    root_elements
        .iter()
        .map(|el| {
            let curr_el = elements.get(el.0).unwrap();
            let created = curr_el.output_amount;
            let multiply = (el.1 + created - 1) / created;

            let ore = curr_el.input_elements.get(0).unwrap(); // ore
            ore.0 * multiply
        })
        .sum()
}

fn get_input_elements<'a>(elements: &'a HashMap<&'a str, CreatedElement>, mut root_elements: &mut HashMap<&'a str, isize>, created_element: &'a CreatedElement, amount: isize) {
    let multiply = (amount + created_element.output_amount - 1) / created_element.output_amount;

    for el in created_element.input_elements.iter() {
        if el.1 == "ORE".to_string() {
            let name = created_element.output_name.as_str();

            match root_elements.get_mut(name) {
                None => {
                    root_elements.insert(name, amount);
                },
                Some(curr_count) => {
                    *curr_count += amount;
                }
            };

        } else {
            let curr_element = elements.get(el.1.as_str()).unwrap();
            get_input_elements(elements, root_elements, curr_element, el.0 * multiply);
        }
    }
}

fn part2(pixels: &mut Vec<(isize, isize)>, start_pixel: (isize, isize)) -> isize {
    2
}

fn parse_input(input: &str) -> HashMap<&str, CreatedElement> {
    input
        .trim()
        .lines()
        .map(|line| {
            let splitted: Vec<&str> = line.split(" => ").collect();

            let requires_elements: Vec<(isize, String)> = splitted
                .get(0)
                .unwrap()
                .split(", ")
                .collect::<Vec<&str>>()
                .iter()
                .map(|el| {
                    let el_amount: Vec<&str> = el
                        .split(" ")
                        .collect();

                    let nr = el_amount.get(0).unwrap().parse::<isize>().unwrap();
                    let element = el_amount.get(1).unwrap();

                    (nr, element.to_string())
                })
                .collect();

            let returns: Vec<&str> = splitted
                .get(1)
                .unwrap()
                .split(" ")
                .collect();

            let returns_nr = returns.get(0).unwrap().parse::<isize>().unwrap();
            let returns_element = returns.get(1).unwrap();

            (*returns_element, CreatedElement {
                output_name: returns_element.to_string(),
                output_amount: returns_nr,
                input_elements: requires_elements
            })
        })
        .collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let input = parse_input(include_str!("day14"));
        let i = part1(&input);
        println!("{}", i);
        assert_eq!(i, 221);
    }

    #[test]
    fn test1() {
        let input = parse_input("9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL");
        let i = part1(&input);
        println!("{}", i);
        assert_eq!(i, 165);
    }

    #[test]
    fn test2() {
        let input = parse_input("157 ORE => 5 NZVS
165 ORE => 6 DCFZ
44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
179 ORE => 7 PSHF
177 ORE => 5 HKGWZ
7 DCFZ, 7 PSHF => 2 XJWVT
165 ORE => 2 GPVTF
3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT");
        let i = part1(&input);
        println!("{}", i);
        assert_eq!(i, 13312);
    }

    #[test]
    fn p2() {
//        let mut input = parse_input(include_str!("day14"));
//        let i = part2(input, (11, 11));
//        assert_eq!(i, 806);
    }
}

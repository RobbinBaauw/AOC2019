use std::collections::HashMap;

struct CreatedElement {
    output_name: String,
    output_amount: i64,
    input_elements: Vec<(i64, String)>
}

fn part1(elements: &HashMap<&str, CreatedElement>) -> i64 {
    compute(elements, 1)
}

fn part2(elements: &HashMap<&str, CreatedElement>) -> i64 {
    let min_fuel = 1000000000000 / compute(elements, 1);

    search(min_fuel, min_fuel * 2, elements)
}

fn search(min: i64, max: i64, elements: &HashMap<&str, CreatedElement>) -> i64 {
    if min == max {
        if compute(elements, min) > 1000000000000 {
            return min - 1;
        }
        return min;
    }

    let mid = (min + max) / 2;
    let required_ores = compute(elements, mid);
    
    match required_ores {
        required_ores if required_ores > 1000000000000 => search(min, mid, elements),
        required_ores if required_ores < 1000000000000 => search(mid + 1, max, elements),
        _ => mid
    }
}

fn compute(elements: &HashMap<&str, CreatedElement>, amount: i64) -> i64 {
    let mut root_elements: HashMap<&str, (i64, i64)> = HashMap::new();
    get_input_elements(elements, &mut root_elements, elements.get("FUEL").unwrap(), amount);
    root_elements.get("ORE").unwrap().0
}

fn get_input_elements<'a>(elements: &'a HashMap<&'a str, CreatedElement>, root_elements: &mut HashMap<&'a str, (i64, i64)>, created_element: &'a CreatedElement, amount: i64) {
    let multiply = (amount + created_element.output_amount - 1) / created_element.output_amount;

    for el in &created_element.input_elements {

        let name = el.1.as_str();
        let mut required_new_amount = el.0 * multiply;

        match root_elements.get_mut(name) {
            None => {
                root_elements.insert(name, (required_new_amount, 0));
            },
            Some(curr_count) => {
                *curr_count = (curr_count.0 + required_new_amount, curr_count.1);
            }
        };

        if el.1 != "ORE" {
            let curr_element = &elements[name];
            let root_element = root_elements[name];

            let next_created_amount = (required_new_amount + curr_element.output_amount - 1) / curr_element.output_amount;
            let mut left_over = (curr_element.output_amount * next_created_amount) - required_new_amount + root_element.1;

            if left_over < 0 {
                unreachable!();
            } else if left_over >= curr_element.output_amount {
                required_new_amount -= curr_element.output_amount;
                left_over -= curr_element.output_amount;
            }

            root_elements.insert(name, (root_element.0, left_over));

            get_input_elements(elements, root_elements, curr_element, required_new_amount);
        }
    }
}

fn parse_input(input: &str) -> HashMap<&str, CreatedElement> {
    input
        .trim()
        .lines()
        .map(|line| {
            let splitted: Vec<_> = line.split(" => ").collect();

            let requires_elements = splitted[0].split(", ").map(|el| {
                let el_amount: Vec<_> = el.split_whitespace().collect();
                let nr = el_amount[0].parse().unwrap();
                let element = el_amount[1];
                (nr, element.to_string())
            }).collect();

            let returns: Vec<_> = splitted[1].split_whitespace().collect();

            let returns_nr = returns[0].parse().unwrap();
            let returns_element = returns[1];

            (returns_element, CreatedElement {
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
        assert_eq!(i, 178154);
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
        assert_eq!(i, 13312);
    }

    #[test]
    fn p2() {
        let input = parse_input(include_str!("day14"));
        let i = part2(&input);
        assert_eq!(i, 6226152);
    }
}

pub fn part1(number: &str) -> String {
    let mut parsed_input: Vec<_> = number
        .chars()
        .map(|c| c.to_digit(10).unwrap() as u8)
        .collect();

    for _ in 0..100 {
        let chars = parsed_input.len();
        let mut result_string: Vec<u8> = vec![0; chars];

        for j in 0..chars {
            let mut curr_sum = 0;
            for (i, curr_char) in parsed_input.iter().enumerate() {
                let curr_number = get_multiplier(i, j + 1) * *curr_char as i32;
                curr_sum += curr_number;
            }

            let last_int = curr_sum
                .to_string()
                .chars()
                .last()
                .unwrap()
                .to_digit(10)
                .unwrap() as u8;

            result_string[j] = last_int;
        }

        parsed_input = result_string;
    }

    parsed_input[..8].iter().map(|c| c.to_string()).collect::<Vec<_>>().join("")
}

pub fn part2(number: &str) -> String {
    let offset = number[..7].parse::<usize>().unwrap();
    let chars = number.len() * 10000;

    let mut parsed_input: Vec<_> = number
        .chars()
        .map(|c| c.to_digit(10).unwrap() as u8)
        .cycle()
        .take(chars)
        .collect();

    for _ in 0..100 {
        let mut result: Vec<u8> = vec![0; chars];
        result[chars - 1] = parsed_input[chars - 1];

        let top = chars - 1;
        let mid = chars / 2;
        for i in (mid..top).rev() {
            let curr_result = parsed_input[i] + result[i + 1]; // triangular matrix
            result[i] = curr_result % 10;
        }
        parsed_input = result;
    }

    parsed_input[offset..offset + 8].iter().map(|c| c.to_string()).collect::<Vec<_>>().join("")
}

fn get_multiplier(index: usize, multiply: usize) -> i32 {
    let mut index_copy = index;
    let mut i = 0;

    while index_copy >= multiply || (i == 0 && index_copy >= multiply - 1) {
        if i == 0 {
            index_copy -= multiply - 1;
        } else {
            index_copy -= multiply;
        }
        i += 1;
    }

    match i % 4 {
        0 => 0,
        1 => 1,
        2 => 0,
        3 => -1,
        _ => unreachable!()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let i = part1("59758034323742284979562302567188059299994912382665665642838883745982029056376663436508823581366924333715600017551568562558429576180672045533950505975691099771937719816036746551442321193912312169741318691856211013074397344457854784758130321667776862471401531789634126843370279186945621597012426944937230330233464053506510141241904155782847336539673866875764558260690223994721394144728780319578298145328345914839568238002359693873874318334948461885586664697152894541318898569630928429305464745641599948619110150923544454316910363268172732923554361048379061622935009089396894630658539536284162963303290768551107950942989042863293547237058600513191659935");
        assert_eq!(i, "30379585");
    }

    #[test]
    fn test1() {
        let i = part1("80871224585914546619083218645595");
        assert_eq!(i, "24176176");
    }

    #[test]
    fn test2() {
        let i = part1("69317163492948606335995924319873");
        assert_eq!(i, "52432133");
    }

    #[test]
    fn test3() {
        let i = part1("19617804207202209144916044189917");
        assert_eq!(i, "73745418");
    }

    #[test]
    fn test4() {
        let i = part1("12345678");
        assert_eq!(i, "73745418");
    }

    #[test]
    fn p2() {
        let i = part2("59758034323742284979562302567188059299994912382665665642838883745982029056376663436508823581366924333715600017551568562558429576180672045533950505975691099771937719816036746551442321193912312169741318691856211013074397344457854784758130321667776862471401531789634126843370279186945621597012426944937230330233464053506510141241904155782847336539673866875764558260690223994721394144728780319578298145328345914839568238002359693873874318334948461885586664697152894541318898569630928429305464745641599948619110150923544454316910363268172732923554361048379061622935009089396894630658539536284162963303290768551107950942989042863293547237058600513191659935");
        assert_eq!(i, "22808931");
    }

    #[test]
    fn test5() {
        let i = part2("03036732577212944063491565474664");
        assert_eq!(i, "84462026");
    }

    #[test]
    fn test6() {
        let i = part2("02935109699940807407585447034323");
        assert_eq!(i, "78725270");
    }

    #[test]
    fn test7() {
        let i = part2("03081770884921959731165446850517");
        assert_eq!(i, "53553731");
    }
}

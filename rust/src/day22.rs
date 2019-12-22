use mod_exp::mod_exp;

pub fn part1(input: &str) -> usize {
    let mut cards = (0..10007).collect::<Vec<_>>();

    input
        .lines()
        .for_each(|l| {
            if l.starts_with("deal into new stack") {
                cards = cards.deal_into_new_stack();
            } else if l.starts_with("cut") {
                let n = l.split("cut ").collect::<Vec<_>>()[1].parse().unwrap();
                cards = cards.cut_n_cards(n);
            } else {
                let n = l.split("deal with increment ").collect::<Vec<_>>()[1].parse().unwrap();
                cards = cards.deal_with_increment_n(n);
            }
        });

    cards.iter().position(|e| *e == 2019).unwrap()
}

// thanks https://www.reddit.com/r/adventofcode/comments/ee0rqi/2019_day_22_solutions/fbnkaju/?context=3
pub fn part2(input: &str) -> i128 {
    let cards: i128 = 119315717514047;
    let repetitions: i128 = 101741582076661;

    let inv = |n: i128| -> i128 {
        mod_exp(n, cards - 2, cards)
    };

    let get = |offset: i128, increment: i128, i: i128| -> i128 {
        (offset + i * increment) % cards
    };

    let mut increment: i128 = 1;
    let mut offset: i128 = 0;

    input
        .lines()
        .for_each(|l| {
            if l.starts_with("deal into new stack") {
                increment *= -1;
                increment %= cards;
                offset += increment;
                offset %= cards;
            } else if l.starts_with("cut") {
                let n = l.split("cut ").collect::<Vec<_>>()[1].parse::<i128>().unwrap();
                offset += n * increment;
                offset %= cards;
            } else {
                let n = l.split("deal with increment ").collect::<Vec<_>>()[1].parse().unwrap();
                increment *= inv(n);
                increment %= cards;
            }
        });

    let final_increment = mod_exp(increment, repetitions, cards);

    let i0 = offset;
    let i1 = inv((1 - increment) % cards);
    let i2 = (1 - final_increment);

    let mut final_offset: i128 = i0 * i1 * i2;
    final_offset %= cards;

    get(final_offset, final_increment, 2020)
}

trait Card {
    fn deal_into_new_stack(&mut self) -> Vec<usize>;
    fn cut_n_cards(&mut self, n: isize) -> Vec<usize>;
    fn deal_with_increment_n(&mut self, n: usize) -> Vec<usize>;
}

impl Card for Vec<usize> {
    fn deal_into_new_stack(&mut self) -> Vec<usize> {
        self.reverse();
        self.to_owned()
    }

    fn cut_n_cards(&mut self, n: isize) -> Vec<usize> {
        if n >= 0 {
            let i= self.len() - n as usize;
            self.rotate_right(i);
        } else {
            self.rotate_right(n.wrapping_abs() as usize);
        }

        self.to_owned()
    }

    fn deal_with_increment_n(&mut self, n: usize) -> Vec<usize> {
        let vec_len = self.len();
        let mut new_vec = Vec::new();
        new_vec.resize(vec_len, 0);

        for (i, val) in self.iter().enumerate() {
            let curr_index = (i * n) % vec_len;
            new_vec[curr_index] = *val;
        }

        new_vec
    }
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn p1() {
        let i = part1(include_str!("day22"));
        assert_eq!(i, 5540);
    }

    #[test]
    fn p2() {
        let i = part2(include_str!("day22"));
        assert_eq!(i, 6821410630991);
    }
}

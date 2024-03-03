public enum BoardSetups {
    SETUP_1 {
        @Override
        public void initialize(Board board) {
        board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
        board.addVehicle(new Vehicle(true, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 12, false, '<'));
        board.addVehicle(new Vehicle(false, false, 3, 4 + Board.PART_MAX_OFFSET_ABS, 0, false, 'a'));
        board.addVehicle(new Vehicle(false, false, 3, 3 + Board.PART_MAX_OFFSET_ABS, 3, true, 'b'));  
        board.addVehicle(new Vehicle(false, false, 2, Board.PART_MAX_OFFSET_ABS, 4, true, 'c')); 
        board.addVehicle(new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 5, true, 'd'));
        board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 6, true, 'e'));
        board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 7, true, 'f'));
        board.addVehicle(new Vehicle(false, false, 2, 3 + Board.PART_MAX_OFFSET_ABS, 8, true, 'g'));
        board.addVehicle(new Vehicle(false, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 9, true, 'h'));
        board.addVehicle(new Vehicle(false, false, 3, Board.PART_MAX_OFFSET_ABS, 10, true, 'i'));
        board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 11, false, 'j'));
        }
    },

    SETUP_2 {
        @Override
        public void initialize(Board board) {
            board.addVehicle(new Vehicle(true, true, 2, 2 + Board.PART_MAX_OFFSET_ABS, 0, false, '>'));
            board.addVehicle(new Vehicle(true, false, 2, 3 + Board.PART_MAX_OFFSET_ABS, 12, false, '<'));
            board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 2, true, 'a'));
            board.addVehicle(new Vehicle(false, false, 3, 2 + Board.PART_MAX_OFFSET_ABS, 11, true, 'b'));
            board.addVehicle(new Vehicle(false, false, 2, Board.PART_MAX_OFFSET_ABS, 6, false, 'c'));
            board.addVehicle(new Vehicle(false, false, 2, Board.PART_MAX_OFFSET_ABS, 3, true, 'd'));
            board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 8, true, 'e'));
            board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 5, true, 'f'));
            board.addVehicle(new Vehicle(false, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 10, true, 'g'));
            board.addVehicle(new Vehicle(false, false, 2, 5 + Board.PART_MAX_OFFSET_ABS, 6, false, 'h'));
        }
    },

    SETUP_3 {
        @Override
        public void initialize(Board board) {
            board.addVehicle(new Vehicle(true, true, 2, 2 + Board.PART_MAX_OFFSET_ABS, 0, false, '>')); // gold hero
            board.addVehicle(new Vehicle(true, false, 2, 3 + Board.PART_MAX_OFFSET_ABS, 12, false, '<')); // silver hero
            board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 2, true, 'a')); // blue car on the left
            board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 3, true, 'b')); // orange car on the left
            board.addVehicle(new Vehicle(false, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 2, false, 'c')); // white car on the left
            board.addVehicle(new Vehicle(false, false, 2, Board.PART_MAX_OFFSET_ABS, 6, false, 'd')); // green car
            board.addVehicle(new Vehicle(false, false, 3, 2 + Board.PART_MAX_OFFSET_ABS, 6, true, 'e')); // red car on the left
            board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 7, true, 'f')); // orange car on the right
            board.addVehicle(new Vehicle(false, false, 2, 5 + Board.PART_MAX_OFFSET_ABS, 6, false, 'g')); // yellow car
            board.addVehicle(new Vehicle(false, false, 3, 2 + Board.PART_MAX_OFFSET_ABS, 10, true, 'h')); // red car on the right
            board.addVehicle(new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 10, false, 'i')); // white car on the right
            board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 11, true, 'j')); // blue car on the right
        }
    },
    
    SETUP_4 {
        @Override
        public void initialize(Board board) {
            board.addVehicle(new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>')); // gold hero
            board.addVehicle(new Vehicle(true, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 12, false, '<')); // silver hero
            board.addVehicle(new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 3, true, 'a')); // red car
            board.addVehicle(new Vehicle(false, false, 2, 0 + Board.PART_MAX_OFFSET_ABS, 4, true, 'b')); // blue car
            board.addVehicle(new Vehicle(false, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 4, true, 'c')); // white car on the left
            board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 5, true, 'd')); // yellow car on the left
            board.addVehicle(new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 8, true, 'e')); // white car on the right
            board.addVehicle(new Vehicle(false, false, 2, 0 + Board.PART_MAX_OFFSET_ABS, 9, true, 'f')); // yellow car on the right
            board.addVehicle(new Vehicle(false, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 9, true, 'g')); // green car
            board.addVehicle(new Vehicle(false, false, 3, 2 + Board.PART_MAX_OFFSET_ABS, 10, true, 'h')); // orange car
        }

    };

    public abstract void initialize(Board board);
}

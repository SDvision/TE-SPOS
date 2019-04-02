package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Parse1 {

    private static final ArrayList<String> IMPERATIVE_STATEMENT = new ArrayList<>(Arrays.asList(
            "STOP","ADD", "SUB", "MULT", "MOVER","MOVEM","COMP","BC","DIV","READ","PRINT"));
    private static final ArrayList<String> USER_REG = new ArrayList<>(Arrays.asList("AREG","BREG","CREG","DREG"));
    private static final ArrayList<String> ASSEMBLER_DIRECTIVES = new ArrayList<>(Arrays.asList(
            "START","END","ORIGIN","EQU","LTORG"));
    private static final ArrayList<String> DECLARE_CONSTANT = new ArrayList<>(Arrays.asList("DC","DS"));
    private static final ArrayList<String> COMPARISION_CONDITION = new ArrayList<>(Arrays.asList(
            "EQ","LT","GT","LE","GE","NE","ANY"));


    private final ArrayList<String> LITERALS = new ArrayList<>();
    private final ArrayList<Symbol> SYMBOLS = new ArrayList<>();
    private int location_counter;
    private int ltorg_index;

    private BufferedReader input_file;
    private PrintWriter intermediate_file;
    private PrintWriter symbol_table_file;
    private PrintWriter literal_table_file;
    private PrintWriter pool_table_file;


    public Parse1(String path) {
        try {
            input_file = new BufferedReader(new FileReader(new File(path)));
            intermediate_file = new PrintWriter(new File("IM.txt"));
            symbol_table_file = new PrintWriter(new File("ST.txt"));
            literal_table_file = new PrintWriter(new File("LT.txt"));
            pool_table_file = new PrintWriter(new File("PT.txt"));

            String line;
            while ((line = input_file.readLine()) != null) {
                String[] tokens = line.split(" ");
                process_tokens(tokens);
            }

            for (int i = 0; i < SYMBOLS.size(); i++) {
                symbol_table_file.println(i+" "+SYMBOLS.get(i).value+" "+SYMBOLS.get(i).address);
            }
            ltorg();


            intermediate_file.close();
            symbol_table_file.close();
            literal_table_file.close();
            pool_table_file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Opcode{
        int id,address;
        String type,value;

        Opcode(int id, String type) {
            this(id,type,null);
        }
        Opcode(int id, String type, String value) {
            this.id = id;
            this.type = type;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            return ((Opcode)obj).id == this.id && ((Opcode) obj).type.equals(this.type);
        }
    }
    class Symbol extends Opcode{

        Symbol(int id, String type, String value) {
            super(id, type, value);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Symbol))return super.equals(obj);
            return this.value.equals(((Symbol) obj).value);
        }
    }



    private Opcode check_category(String token){
        int idx;
        if((idx = IMPERATIVE_STATEMENT.indexOf(token)) > 0) return new Opcode(idx,"IS");
        if((idx = USER_REG.indexOf(token)+1) > 0) return new Opcode(idx,"RG");
        if((idx = ASSEMBLER_DIRECTIVES.indexOf(token)+1) > 0) return new Opcode(idx,"AD");
        if((idx = DECLARE_CONSTANT.indexOf(token)+1) > 0) return new Opcode(idx,"DL");
        if((idx = COMPARISION_CONDITION.indexOf(token)+1) > 0) return new Opcode(idx,"CC");
        if (token.matches("\\d*")) return new Opcode(Integer.parseInt(token),"C");
        if(token.startsWith("=")) {
            if (!LITERALS.contains(token)) LITERALS.add(token);
            return new Opcode(LITERALS.indexOf(token),"L");
        }

        Symbol symbol = new Symbol(SYMBOLS.size(),"S",token);
        if(!SYMBOLS.contains(symbol)) {
            SYMBOLS.add(symbol);
            return symbol;
        }
        SYMBOLS.get(SYMBOLS.indexOf(symbol)).id = SYMBOLS.indexOf(symbol);
        return SYMBOLS.get(SYMBOLS.indexOf(symbol));

    }



    private void process_tokens(String[] tokens){

        ArrayList<Opcode> opcodes = new ArrayList<>();


        for (String token : tokens) opcodes.add(check_category(token));

        int start_idx = opcodes.indexOf(new Opcode(1,"AD"));
        if (start_idx>=0) location_counter = opcodes.get(start_idx+1).id;

        int dc_idx = opcodes.indexOf(new Opcode(1,"DL"));
        if (dc_idx>=0) opcodes.get(dc_idx-1).address = location_counter;

        int ds_idx = opcodes.indexOf(new Opcode(2,"DL"));
        if (ds_idx>=0) {
            opcodes.get(ds_idx-1).address = location_counter;
            location_counter += opcodes.get(ds_idx+1).id - 1;
        }

        if(opcodes.contains(new Opcode(5,"AD"))) ltorg();

        boolean isAD = false;
        for (Opcode opcode: opcodes) if(opcode.type.equals("AD")) isAD = true;
        if (isAD) location_counter--; else if (location_counter!= 0) intermediate_file.print(location_counter+" ");
        for (Opcode opcode: opcodes) intermediate_file.print(String.format("(%s,%s) ",opcode.type,opcode.id));



        location_counter++;
        intermediate_file.println();
    }

    private void ltorg(){
        pool_table_file.println(ltorg_index);
        for (int i = 0; i < LITERALS.size(); i++) {
            literal_table_file.println(ltorg_index+++" "+LITERALS.get(i)+" "+location_counter++);
        }
        LITERALS.clear();
    }


}

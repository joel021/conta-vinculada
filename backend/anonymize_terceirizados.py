#!/usr/bin/env python3
"""
anonymize_terceirizados.py

Script to anonymize personal data in 'setup_terceirizados.csv'.
Replaces:
  - 'Nome do empregado' column values with random Brazilian names
    in the form "FirstName MotherName FatherName" (3 parts).
  - 'CPF' column values with random numbers matching the format "000000000-00"
    (9 numbers '-' followed by two numbers, total 11 numbers).
  - Also cleans substitute employee names and CPFs when present,
    and updates auxiliary CPF columns to maintain spreadsheet consistency,
    ensuring all real personal data is completely removed.
"""

import argparse
import os
import random
import re
import sys
from pathlib import Path
from typing import List, Optional, Set, Tuple

# Common Brazilian First Names (Given Names)
FIRST_NAMES = [
    # Male
    "Lucas", "Gabriel", "Mateus", "Pedro", "Guilherme", "Gustavo", "Felipe", "Rafael",
    "Leonardo", "Rodrigo", "Bruno", "Thiago", "Diego", "Eduardo", "Marcelo", "Alexandre",
    "Caio", "Danilo", "Fabio", "Fernando", "Henrique", "Igor", "Joao", "Julio", "Leandro",
    "Marcos", "Otavio", "Paulo", "Renato", "Ricardo", "Samuel", "Vinicius", "Vitor",
    "Andre", "Arthur", "Bernardo", "Daniel", "Davi", "Heitor", "Lorenzo", "Caua",
    "Francisco", "Murilo", "Enzo", "Lucca", "Joaquim", "Bryan", "Nicolas", "Vicente",
    "Antonio", "Carlos", "Jose", "Manoel", "Sebastiao", "Geraldo", "Raimundo", "Benedito",
    "Sergio", "Marcio", "Cristiano", "Rogerio", "Claudio", "Wagner", "Robson", "Anderson",
    # Female
    "Maria", "Ana", "Beatriz", "Camila", "Carolina", "Debora", "Eduarda", "Fernanda",
    "Gabriela", "Helena", "Isabela", "Juliana", "Larissa", "Leticia", "Luana", "Mariana",
    "Natalia", "Patricia", "Rafaela", "Rebeca", "Sabrina", "Tatiane", "Vanessa", "Yasmin",
    "Amanda", "Bianca", "Bruna", "Clarice", "Daniela", "Flavia", "Giovanna", "Jessica",
    "Laura", "Luiza", "Manuela", "Milena", "Priscila", "Renata", "Sophia", "Valentina",
    "Alice", "Lorena", "Cecilia", "Livia", "Antonella", "Isadora", "Mirella", "Talita",
    "Francisca", "Aparecida", "Antonia", "Raimunda", "Rita", "Lucia", "Sonia", "Terezinha",
    "Luciana", "Aline", "Simone", "Adriana", "Claudia", "Renata", "Monica", "Eliane"
]

# Common Brazilian Surnames (Family Names)
SURNAMES = [
    "Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira",
    "Lima", "Gomes", "Costa", "Ribeiro", "Martins", "Carvalho", "Almeida", "Lopes",
    "Soares", "Fernandes", "Vieira", "Barbosa", "Rocha", "Dias", "Nascimento", "Andrade",
    "Moreira", "Nunes", "Marques", "Machado", "Mendes", "Freitas", "Cardoso", "Ramos",
    "Goncalves", "Santana", "Teixeira", "Pinto", "Cavalcanti", "Castro", "Campos", "Barros",
    "Moraes", "Monteiro", "Batista", "Pacheco", "Dantas", "Borges", "Guedes", "Miranda",
    "Coelho", "Vasconcelos", "Brito", "Azevedo", "Magalhaes", "Tavares", "Figueiredo", "Duarte",
    "Guimaraes", "Fonseca", "Siqueira", "Pinheiro", "Sampaio", "Reis", "Macedo", "Neves",
    "Bezerra", "Peixoto", "Sales", "Vargas", "Farias", "Carneiro", "Pires", "Cunha",
    "Medeiros", "Barreto", "Aguiar", "Passos", "Fontes", "Viana", "Bandeira", "Amaral"
]


def generate_brazilian_name() -> str:
    """
    Generates a realistic Brazilian full name with three parts:
    FirstName MotherSurname FatherSurname.
    Always returns uppercase to match official record standards.
    """
    first_name = random.choice(FIRST_NAMES)
    mother_surname, father_surname = random.sample(SURNAMES, 2)
    return f"{first_name} {mother_surname} {father_surname}".upper()


def generate_cpf(used_cpfs: Set[str]) -> str:
    """
    Generates a unique Brazilian CPF matching the format '000000000-00'
    (9 numbers '-' followed by two numbers, total 11 numbers).
    The two check digits are calculated using the official Modulo-11 algorithm
    so the generated CPF is valid and passes system validations.
    """
    while True:
        # Generate 9 random base digits
        digits = [random.randint(0, 9) for _ in range(9)]
        
        # Discard repeated digits like '111111111'
        if len(set(digits)) == 1:
            continue

        # Calculate first check digit (v1)
        s1 = sum(d * factor for d, factor in zip(digits, range(10, 1, -1)))
        r1 = s1 % 11
        v1 = 0 if r1 < 2 else 11 - r1
        digits.append(v1)

        # Calculate second check digit (v2)
        s2 = sum(d * factor for d, factor in zip(digits, range(11, 1, -1)))
        r2 = s2 % 11
        v2 = 0 if r2 < 2 else 11 - r2

        # Format: 9 numbers '-' 2 numbers -> 000000000-00
        num_part = "".join(str(d) for d in digits[:9])
        verif_part = f"{v1}{v2}"
        cpf_formatted = f"{num_part}-{verif_part}"

        if cpf_formatted not in used_cpfs:
            used_cpfs.add(cpf_formatted)
            return cpf_formatted


def extract_middle_digits(cpf_str: str) -> str:
    """
    Extracts the middle 6 digits of a CPF without leading zeros,
    matching the auxiliary CPF column representation in the original sheet.
    Example: '010982375-36' -> digits '010 982375 36' -> '982375'
    """
    clean_digits = re.sub(r"\D", "", cpf_str)
    if len(clean_digits) >= 9:
        middle_part = clean_digits[3:9].lstrip("0")
        return middle_part if middle_part else "0"
    return ""


def resolve_input_file(specified_path: Optional[str]) -> Path:
    """
    Locates the setup_terceirizados.csv file.
    Checks:
      1. Explicitly provided CLI argument.
      2. ./src/main/resources/setup_terceirizados.csv
      3. ./src/main/javsa/resources/setup_terceirizados.csv (handles user prompt typo)
      4. Workspace searches relative to script location.
    """
    if specified_path:
        p = Path(specified_path)
        if p.is_file():
            return p
        raise FileNotFoundError(f"Specified file not found: {specified_path}")

    candidates = [
        Path("./src/main/resources/setup_terceirizados.csv"),
        Path("./src/main/javsa/resources/setup_terceirizados.csv"),
        Path(__file__).resolve().parent / "src" / "main" / "resources" / "setup_terceirizados.csv",
        Path(__file__).resolve().parent / "src" / "main" / "javsa" / "resources" / "setup_terceirizados.csv",
        Path("./target/classes/setup_terceirizados.csv"),
    ]

    for cand in candidates:
        if cand.is_file():
            return cand.resolve()

    # Search current directory recursively
    for found in Path(".").rglob("setup_terceirizados.csv"):
        if found.is_file():
            return found.resolve()

    raise FileNotFoundError("Could not find 'setup_terceirizados.csv' in expected directories.")


def is_blank_or_space(val: str) -> bool:
    """Checks if a cell is empty, only spaces, or non-breaking spaces (\u00a0)."""
    cleaned = val.replace("\u00a0", " ").strip()
    return len(cleaned) == 0


def anonymize_file(
    input_path: Path,
    output_path: Optional[Path] = None,
    create_backup: bool = False,
    anonymize_substitutes: bool = True,
    seed: Optional[int] = None
) -> Tuple[int, int, int]:
    """
    Reads the CSV, anonymizes 'Nome do empregado' and 'CPF' columns,
    and writes back the updated content.

    Returns:
      (total_rows_processed, employees_anonymized, substitutes_anonymized)
    """
    if seed is not None:
        random.seed(seed)

    if output_path is None:
        output_path = input_path

    # Read all lines
    with open(input_path, "r", encoding="utf-8") as f:
        lines = f.readlines()

    if not lines:
        raise ValueError(f"File {input_path} is empty.")

    header_line = lines[0].rstrip("\r\n")
    header_cols = header_line.split(",")

    # Identify column indices
    # 1. 'Nome do empregado'
    try:
        col_nome_emp = header_cols.index("Nome do empregado")
    except ValueError:
        col_nome_emp = 3  # fallback default index

    # 2. 'CPF' columns
    cpf_indices = [idx for idx, name in enumerate(header_cols) if name.strip() == "CPF"]
    col_cpf_emp = cpf_indices[0] if len(cpf_indices) > 0 else 4
    col_cpf_emp_mid = cpf_indices[1] if len(cpf_indices) > 1 else 6
    col_cpf_sub = cpf_indices[2] if len(cpf_indices) > 2 else 13
    col_cpf_sub_mid = cpf_indices[3] if len(cpf_indices) > 3 else 14

    # 3. Substitute employee name
    try:
        col_nome_sub = header_cols.index("Nome do Empregado Substituto no Período de Ausência")
    except ValueError:
        col_nome_sub = 12  # fallback default index

    used_cpfs: Set[str] = set()
    processed_lines = [header_line]

    emp_count = 0
    sub_count = 0

    for line_idx, line in enumerate(lines[1:], start=2):
        raw_line = line.rstrip("\r\n")
        if not raw_line:
            continue

        cols = raw_line.split(",")

        # Ensure line has expected columns
        while len(cols) < len(header_cols):
            cols.append("")

        # 1. Anonymize Employee Name ('Nome do empregado')
        if col_nome_emp < len(cols) and not is_blank_or_space(cols[col_nome_emp]):
            cols[col_nome_emp] = generate_brazilian_name()

        # 2. Anonymize Employee CPF ('CPF' column in format 000000000-00)
        if col_cpf_emp < len(cols) and not is_blank_or_space(cols[col_cpf_emp]):
            new_emp_cpf = generate_cpf(used_cpfs)
            cols[col_cpf_emp] = new_emp_cpf
            # Keep auxiliary middle-digits column consistent
            if col_cpf_emp_mid < len(cols) and not is_blank_or_space(cols[col_cpf_emp_mid]):
                cols[col_cpf_emp_mid] = extract_middle_digits(new_emp_cpf)
            emp_count += 1

        # 3. Anonymize Substitute Employee Name and CPF (if present and non-empty)
        if anonymize_substitutes:
            has_sub_name = (col_nome_sub < len(cols) and not is_blank_or_space(cols[col_nome_sub]))
            has_sub_cpf = (col_cpf_sub < len(cols) and not is_blank_or_space(cols[col_cpf_sub]))

            if has_sub_name:
                cols[col_nome_sub] = generate_brazilian_name()

            if has_sub_cpf:
                new_sub_cpf = generate_cpf(used_cpfs)
                cols[col_cpf_sub] = new_sub_cpf
                if col_cpf_sub_mid < len(cols) and not is_blank_or_space(cols[col_cpf_sub_mid]):
                    cols[col_cpf_sub_mid] = extract_middle_digits(new_sub_cpf)
                sub_count += 1

        processed_lines.append(",".join(cols))

    # Backup original file if requested and output is in-place
    if create_backup and input_path.resolve() == output_path.resolve():
        backup_path = input_path.with_name(input_path.name + ".bak")
        if not backup_path.exists():
            with open(backup_path, "w", encoding="utf-8") as f_bak:
                f_bak.writelines(lines)
            print(f"Created backup at: {backup_path}")

    # Write output
    with open(output_path, "w", encoding="utf-8") as f_out:
        for idx, pline in enumerate(processed_lines):
            # Write with standard newline
            if idx == len(processed_lines) - 1 and not lines[-1].endswith("\n"):
                # Preserve no-newline-at-EOF if original didn't have it
                f_out.write(pline)
            else:
                f_out.write(pline + "\n")

    return len(lines) - 1, emp_count, sub_count


def main():
    parser = argparse.ArgumentParser(
        description="Anonymize 'Nome do empregado' and 'CPF' in setup_terceirizados.csv."
    )
    parser.add_argument(
        "file",
        nargs="?",
        default=None,
        help="Path to setup_terceirizados.csv (auto-detected if omitted)."
    )
    parser.add_argument(
        "-o", "--output",
        default=None,
        help="Path to save output CSV (defaults to overwriting input in-place)."
    )
    parser.add_argument(
        "--backup",
        action="store_true",
        help="Create a .bak backup file before overwriting (off by default to avoid keeping sensitive data)."
    )
    parser.add_argument(
        "--no-substitutes",
        action="store_true",
        help="Do not anonymize substitute employee columns."
    )
    parser.add_argument(
        "--seed",
        type=int,
        default=None,
        help="Random seed for reproducible results."
    )

    args = parser.parse_args()

    try:
        input_file = resolve_input_file(args.file)
    except FileNotFoundError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)

    output_file = Path(args.output).resolve() if args.output else input_file

    print(f"Opening input file: {input_file}")
    total_rows, emp_count, sub_count = anonymize_file(
        input_path=input_file,
        output_path=output_file,
        create_backup=args.backup,
        anonymize_substitutes=not args.no_substitutes,
        seed=args.seed
    )

    print(f"Successfully processed {total_rows} data rows.")
    print(f"- Employee names & CPFs randomized: {emp_count}")
    if not args.no_substitutes:
        print(f"- Substitute employee names & CPFs randomized: {sub_count}")
    print(f"Output saved to: {output_file}")


if __name__ == "__main__":
    main()

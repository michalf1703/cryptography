package com.example.des3;
import java.util.*;
import java.lang.*;


public class DES
{   class DESKeyException extends Exception
{public DESKeyException(String wiadomosc){super(wiadomosc);};
}
    public DES()
    {try{setKeyHex("0123456789ABCDEF");}catch (DESKeyException e){};
    }
    BitOperations bicik;
    String string_klucz;
    byte[] byte_klucz;
    byte podklucze[][];
    //tablica do wyznaczania prawego/lewego bloku
    byte shift[] = {1, 3, 5, 7, 0, 2, 4, 6};
    final byte[] permutacja_pBloku =
            {
                    16, 7, 20, 21,
                    29, 12, 28, 17,
                    1, 15, 23, 26,
                    5, 18, 31, 10,
                    2, 8, 24, 14,
                    32, 27, 3, 9,
                    19, 13, 30, 6,
                    22, 11, 4, 25
            };

    // permutacja początkowa
    final byte[] IP = new byte[] {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    //permutacja końcowa
    final byte[] IPplus = new byte[] {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};


    //w sBlokach podczas szyfrowania ma miejsce operacja podstawienia; za kazde sześć bitów wejściwych podstawia się cztery bity wyjściowe
    final byte[] podstawienieBox =
            {
                    14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, // S1
                    0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
                    4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
                    15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13,

                    15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, // S2
                    3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                    0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                    13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9,

                    10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, // S3
                    13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                    13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                    1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12,

                    7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, // S4
                    13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                    10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                    3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14,

                    2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, // S5
                    14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                    4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                    11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3,

                    12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, // S6
                    10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                    9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                    4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13,

                    4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, // S7
                    13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                    1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                    6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12,

                    13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, // S8
                    1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                    7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                    2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
            };

    //Ustawia klucz ze stringa którego wartości są interpretowane jako zapis HEX
    //dodatkowo sprawdza poprawnosc klucza
    public void setKeyHex(String klucz) throws DESKeyException
    {
        this.byte_klucz = bicik.hexToBytes(klucz);
        if (testKlucza())
        {   this.string_klucz = klucz;
            podklucze = getSubkeys();
        }

    }

    //sprawdza poprawność klucza; klucz musi mieć długość równą 64 bity (8 bajtów)
    public boolean testKlucza() throws DESKeyException
    {
        if (this.byte_klucz == null)
            throw new DESKeyException("Klucz nie może być zerem!");
        else
        {
            int l = this.byte_klucz.length;
            if (l < 8)
            {
                this.byte_klucz = null;
                throw new DESKeyException("Klucz jest za krótki");
            }
            else if (l > 8)
            {
                this.byte_klucz = null;
                throw new DESKeyException("Klucz jest za długi");
            }
            return true;
        }
    }
//byte [] tab -> dane do zaszyfrowania,poczatkowyIndex -> indeks poczatku bloku w tablicy tab
    public byte[] zakodujBlok(byte[] tab, int poczatkowyIndex, int runda) throws Exception
    {
        byte[] wiadomosc = new byte[8];
        System.arraycopy(tab, poczatkowyIndex, wiadomosc, 0, 8);
        return szyfrowanie(wiadomosc,runda);
    }

    public byte[] odkodujBlok(byte[] tab, int poczatkowyIndex, int runda) throws Exception
    {
        byte[] wiadomosc = new byte[8];
        System.arraycopy(tab, poczatkowyIndex, wiadomosc, 0, 8);
        return deszyfrowanie(wiadomosc, runda);
    }

    //szyfruje 64 bity - wchodzi 64 bitowy blok tekstu jawnego ---> wychodzi 64 bitowy blok szyfru
    private byte[] szyfrowanie(byte[] wiadomosc, int runda) throws Exception {
        if (wiadomosc.length != 8) {
            throw new Exception("Część wiadomości nie ma 8 bajtów długości");
        }
        //perumtacja poczatkowa, jesli jest to pierwsza opracja
        if (runda == 1) {
            wiadomosc = bicik.selectBits(wiadomosc, IP );
        }

        byte[] prawy = wyznaczPrawyBlok(wiadomosc);
        byte[] lewy = wyznaczLewyBlok(wiadomosc);
        for (int k = 0; k < 16; k++) {
            byte[] rBackup = prawy;
            prawy = rozszerzBlok(prawy);
            prawy = bicik.xor(prawy, podklucze[k]);
            prawy = podstawienieSblok(prawy);
            prawy = bicik.selectBits(prawy, permutacja_pBloku);
            prawy = bicik.xor(lewy, prawy);
            lewy = rBackup;
        }
        byte[] lewy_prawy = koncowyBlok(lewy, prawy);
        //permutacja koncowa po ostatniej operacji
        if (runda == 3) {
            lewy_prawy = bicik.selectBits(lewy_prawy, IPplus );
        }
        return lewy_prawy;
    }

    //deszyfruje 64 bity szyfrogramu
    private byte[] deszyfrowanie(byte[] wiadomosc, int runda) throws Exception {
        if (wiadomosc.length != 8) {
            throw new Exception("Część wiadomości nie ma 8 bajtów długości");
        }

        // Permutacja początkowa
        if(runda == 1) {
            wiadomosc = bicik.selectBits(wiadomosc, IP );
        }
        byte[] prawy = wyznaczPrawyBlok(wiadomosc);
        byte[] lewy = wyznaczLewyBlok(wiadomosc);
        int liczbaPodkluczy = podklucze.length;

        for (int k = 0; k < liczbaPodkluczy; k++) {
            byte[] rBackup = prawy;
            prawy = rozszerzBlok(prawy);
            prawy = bicik.xor(prawy, podklucze[liczbaPodkluczy - k - 1]);
            prawy = podstawienieSblok(prawy);
            prawy = bicik.selectBits(prawy, permutacja_pBloku);
            prawy = bicik.xor(lewy, prawy);
            lewy = rBackup;
        }

        byte[] left_right = koncowyBlok(lewy, prawy);

        // Permutacja końcowa
        if(runda == 3) {
            left_right = bicik.selectBits(left_right, IPplus );
        }

        return left_right;
    }

    //wykonujemy operacje bitowe tak aby bity odpowiedzialne za lewą stronę bloku znalazły się
    //na pozycjach 0-31 (czyli w pierwszych 4 bajtach), a bity odpowiedzialne za prawą stronę na pozycjach 32-63 (czyli w kolejnych 4 bajtach)
    private byte[] wyznaczLewyBlok(byte[] blokWejsciowy)
    {
        //tworzymy pusty 4-bajtowy bufor
        byte[] blokWyjsciowy = new byte[4];
        byte aktualnie;
        for (byte liczBajty = 4; liczBajty < 8; liczBajty++)
        {
            for (byte liczBity = 7; liczBity >= 0; liczBity--)
            {
                aktualnie = (byte) (blokWejsciowy[liczBity] >>> shift[liczBajty]);
                aktualnie = (byte) (aktualnie & 1);
                aktualnie = (byte) (aktualnie << (liczBity));
                blokWyjsciowy[7 - liczBajty] = (byte) (blokWyjsciowy[7 - liczBajty] | aktualnie);
            }
        }
        return blokWyjsciowy;
    }

    private byte[] wyznaczPrawyBlok(byte[] blokWejsciowy)
    {
        //tworzymy pusty 4-bajtowy bufor
        byte[] blokWyjsciowy = new byte[4];
        byte aktualnie;
        //dla kazdego bitu w bajcie
        for (byte liczBajty = 0; liczBajty < 4; liczBajty++)
        {   //zacznyamy od najbardziej znaczącego bitu (7)
            for (byte liczBity = 7; liczBity >= 0; liczBity--)
            {   //wykonujemy przesunięcie bitowe w prawo o określoną przez wartosc tablic shift, liczBajty -> indeks aktualnie przetwarzanego bajtu
                aktualnie = (byte) (blokWejsciowy[liczBity] >>> shift[liczBajty]);
                //ustawianie wartosci na najmniejszy znaczacy bit i dodawanie go do prawgo bloku wyjsciowego
                aktualnie = (byte) (aktualnie & 1);
                aktualnie = (byte) (aktualnie << (liczBity));
                blokWyjsciowy[3 - liczBajty] = (byte) (blokWyjsciowy[3 - liczBajty] | aktualnie);
            }
        }
        //zwracamy blok wyjsciowy
        return blokWyjsciowy;
    }

    //TUTAJ JUZ WSZYTKIE OPERACJE BITOWE
    //nie używamy tablicy premutacji E, tylko odpowiednich operacji bitowych, ktore pozwalają nam rozszerzyć blok z 32 na 48 bitów
    private byte[] rozszerzBlok(byte[] blok)
    {
        //tworzymy pusta tablice o dlugosci 6 bajtów, ktora zostanie wypełniona rozszerzonym blokiem danych
        byte rozszerzonyBlok[] = new byte[6];
        short aktualnie;
        byte pBit = 31;
        byte zmien = 0;
        //iteracja przez każde z 48 bitów bloku danych
        for (int bit = 0; bit < 48; bit++)
        {
            //przesunięcie bitu na pozycji pBit%8 z bajtu blok[pBit / 8] na pozycje 7 - (bit %8)
            aktualnie = (short) (blok[pBit / 8] >> (7 - (pBit % 8)));
            //zapisanie najmłodszego bitu
            aktualnie = (short) (aktualnie & 1);
            //zapisanie najmłodszego bitu na pozycje (7 - (bit %8)) w odpowiednim bajcie tablicy rozszerzonyBlok
            aktualnie = (short) (aktualnie << (7 - (bit % 8)));
            rozszerzonyBlok[bit / 8] = (byte) (rozszerzonyBlok[bit / 8] | (aktualnie));
            //sprawdza czy dodanie 1 do zmien, spowoduje osiagniecie wartosci 6
            if (++zmien == 6)
            {
                //jesli tak to ustawia zmien na 0 i dekrementuje pBit
                zmien = 0;
                pBit--;
            }
            else //w przeciwnym razie inkrementuje pBit modulo 32
                pBit = (byte) ((++pBit) % 32);
        }
        return rozszerzonyBlok;
    }


    private byte[] koncowyBlok(byte[] lewy, byte[] prawy)
    {
        byte[] tab = new byte[8];
        byte[] wynik = new byte[8];
        byte aktualnie;
        System.arraycopy(lewy, 0, tab, 4, 4);
        System.arraycopy(prawy, 0, tab, 0, 4);
        int aktualnyBit = 0;
        for (int pozycjaBitu = 7; pozycjaBitu >= 0; pozycjaBitu--)
        {
            for (int pozycjaBajtu = 4; pozycjaBajtu < 8; pozycjaBajtu++)
            {
                aktualnie = (byte) (tab[pozycjaBajtu] >> (7 - (pozycjaBitu)));
                aktualnie = (byte) (aktualnie & 1);
                aktualnie = (byte) (aktualnie << (7 - (aktualnyBit % 8)));
                wynik[aktualnyBit / 8] = (byte) (wynik[aktualnyBit / 8] | aktualnie);
                aktualnyBit += 2;
            }
        }
        aktualnyBit = 1;
        for (int pozycjaBitu = 7; pozycjaBitu >= 0; pozycjaBitu--)
        {
            for (int readBytePos = 0; readBytePos < 4; readBytePos++)
            {
                aktualnie = (byte) (tab[readBytePos] >> (7 - (pozycjaBitu)));
                aktualnie = (byte) (aktualnie & 1);
                aktualnie = (byte) (aktualnie << (7 - (aktualnyBit % 8)));
                wynik[aktualnyBit / 8] = (byte) (wynik[aktualnyBit / 8] | aktualnie);
                aktualnyBit += 2;
            }
        }
        return wynik;
    }

    //zmiana 6-bitowy blok na 4 bitowy blok, wykorzytujemy skrzynki s-blok
    //4 bity służa jako wiersze,a 2 bity służą jako kolumna
    private byte[] podstawienieSblok(byte[] tab)
    {
        byte rzad;
        byte kolumna;
        //tworzymy tablice 6 bitowych blokow danych
        tab = stworz6bitowyBlok(tab);
        byte[] wynik = new byte[tab.length / 2];
        byte lowerHalfByte = 0;
        byte halfByte;
        for (int b = 0; b < tab.length; b++)
        {
            //dla kazdego bloku wyznaczamy numer wiersza i kolumny w odpowiednim S-bloku
            rzad = (byte) (((tab[b] >> 6) & 2) | ((tab[b] >> 2) & 1));
            kolumna = (byte) ((tab[b] >> 3) & 15);
            halfByte = podstawienieBox[64 * b + 16 * rzad + kolumna];
            if (b % 2 == 0)
                lowerHalfByte = halfByte;
            else
                wynik[b / 2] = (byte) (16 * lowerHalfByte + halfByte);
        }
        return wynik;
    }


    private byte[] stworz6bitowyBlok(byte[] data)
    {
        int numOfBytes = (8 * data.length - 1) / 6 + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < numOfBytes; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                int val = bicik.getBit(data, 6 * i + j);
                bicik.setBit(out, 8 * i + j, val);
            }
        }
        return out;
    }


    public byte[] kodowanie(byte[] message, int runda)
    {
        int len;
        //sprawdzamy dlugosc zaszyfrowanej wiadomosci
        if ((message.length / 2 % 4) != 0)
            len = (message.length / 8 + 1) * 8;
        else
            len = message.length;
        //tworzymy tablice bajtow "result" o dlugosci len
        byte[] result = new byte[len];
        //blok o dlugosci 8 bajtow
        byte[] tempBlock = new byte[8];
        byte[] rawData;
        try {
            rawData = message;
            //iteracja po wiadomosci, po blokach 8 bajtow i szyfrowanie kazdego bloku
            for (int i = 0; i < (rawData.length / 8); i++)
            {
                tempBlock = zakodujBlok(rawData, i * 8,runda);
                //zaszyfrowany blok kopiujemy do tablicy "result" za pomoca "arraycopy"
                System.arraycopy(tempBlock, 0, result, i * 8, 8);
            }
            if (message.length / 2 % 4 != 0)
            {
                for (int i = 0; i < 8; i++)
                {
                    if (i + (rawData.length / 8) * 8 < rawData.length)
                        //uzupelnianie bajtami z wiadomosci
                        tempBlock[i] = rawData[i + (rawData.length / 8) * 8];
                    else
                        //dopelnianie zerami
                        tempBlock[i] = 0;
                }
                tempBlock = zakodujBlok(tempBlock, 0, runda);
                System.arraycopy(tempBlock, 0, result, (rawData.length / 8) * 8, 8);
            }
            return result;
        } catch (Exception ex) {};
        return null;
    }

    public byte[] dekodowanie(byte[] encrypted, int runda)
    {
        byte[] tmpResult = new byte[encrypted.length];
        byte[] tempBlock;
        byte[] rawData;
        try {
            rawData = encrypted;
            for (int i = 0; i < (rawData.length / 8); i++)
            {
                tempBlock = odkodujBlok(rawData, i * 8, runda);
                System.arraycopy(tempBlock, 0, tmpResult, i * 8, tempBlock.length);
            }
            int cnt = 0;
            for (int i = 1; i < 9; i += 2)
            {
                if (tmpResult[tmpResult.length - i] == 0 && tmpResult[tmpResult.length - i - 1] == 0)
                    cnt += 2;
                else
                    break;
            }
            byte[] result = new byte[tmpResult.length - cnt];
            System.arraycopy(tmpResult, 0, result, 0, tmpResult.length - cnt);
            return result;
        } catch (Exception ex) { };
        return null;
    }

///////////////////////GENEROWANIE PODKLUCZY//////////////
//początkowo 64 bitowy klucz jest redukowany do 56-bitowego klucza przez pominiecie bitow parzystosci
// poprzez premutacje klucza według tablic PC1 powstaje klucz 56 bitowy
//po wycieciu 56 bitow klucza, za kazdym razem inny 48 bitowy klucz jest generowany dla kazdego z 16 cykli algorytmu DES
//aby takowe podklucze wygenerowac to 56 bitow klucza dzieli sie na połowy po 28 bitów
//następnie te połowy są przesuwane w lewo o jeden lub dwa w zależności od numeru cyklu
//przesunięcia klucza w zależności od cyklu zostały przestawione w final byte[] SHIFTS
//tworzymy tym samym 16 bloków z jednej połówki oraz 16 bloków z drugiej połówi (16 PAR bloków)
//kazda para bloków tworzona jest z poprzedniego, poprzez przesunięcia w lewo.
//przesunięcia w lewo polegają na przesunięciu każdego bitu w lewo, oprócz peirwszego, który przenoszony jest na koniec bloku
//po wykonaniu operacji przesunięcia jest wybierane 48 bitów z 56
//rozumiem to w ten sposób, że każdy blok ma 28 bajtów, łączymy ponownie prawą stronę z lewą i ponownie mamy 56 bitow, z ktorych musimy wybrac 48
//w ten sposób chcemy stworzyć 16 48-bitowych podkluczy, a wykonujemy to poprzez premutacje tablicy stworzonej z połaczonej tablicy prawego i lewego bloku
//premutacja ta odbywa się według tablicy PC-2
//operacja ta jest nazywana premutacją z kompresją, ponieważ dokonuje zmian w porządku występowania bitów
//permutacja ta
    public byte[][] getSubkeys() throws DESKeyException
    {
        //poczatkowy klucz 64 bitowy jest premutowany wedlug tej tablic i otrzymujemy klucz 56 bitowy
        final byte[] PC1 =
                {
                        57, 49, 41, 33, 25, 17, 9,
                        1, 58, 50, 42, 34, 26, 18,
                        10, 2, 59, 51, 43, 35, 27,
                        19, 11, 3, 60, 52, 44, 36,
                        63, 55, 47, 39, 31, 23, 15,
                        7, 62, 54, 46, 38, 30, 22,
                        14, 6, 61, 53, 45, 37, 29,
                        21, 13, 5, 28, 20, 12, 4
                };
        //według tej tablicy odbywa się premutacja z kompresją
        final byte[] PC2 =
                {
                        14, 17, 11, 24, 1, 5,
                        3, 28, 15, 6, 21, 10,
                        23, 19, 12, 4, 26, 8,
                        16, 7, 27, 20, 13, 2,
                        41, 52, 31, 37, 47, 55,
                        30, 40, 51, 45, 33, 48,
                        44, 49, 39, 56, 34, 53,
                        46, 42, 50, 36, 29, 32
                };
        //przesunięcia klucza w zależności od cyklu
        final byte[] SHIFTS =  { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

        byte[] aktualnyKlucz = bicik.selectBits(this.byte_klucz, PC1);
        byte[] c = bicik.selectBits(aktualnyKlucz, 0, 28);
        byte[] d = bicik.selectBits(aktualnyKlucz, 28, 28);
        byte[][] subKeysLocal = new byte[16][];
        for (int k = 0; k < 16; k++)
        {
            c = bicik.przesunWlewo(c, 28, SHIFTS[k]);
            d = bicik.przesunWlewo(d, 28, SHIFTS[k]);
            byte[] cd = polaczBajty(c, 28, d, 28);
            subKeysLocal[k] = bicik.selectBits(cd, PC2);
        }
        return subKeysLocal;
    }


    //łączenie dwóch ciągów bajtów w jeden, czyli tutaj łączymy prawy blok klucza z lewym
    private byte[] polaczBajty(byte[] a, int aLen, byte[] b, int bLen)
    {
        int numOfBytes = (aLen + bLen - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        int j = 0;
        for (int i = 0; i < aLen; i++)
        {
            int val = bicik.getBit(a, i);
            bicik.setBit(out, j, val);
            j++;
        }
        for (int i = 0; i < bLen; i++)
        {
            int val = bicik.getBit(b, i);
            bicik.setBit(out, j, val);
            j++;
        }
        return out;
    }

    public String generujKlucze() {
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder();
    while (stringBuilder.length() < 16) {
        stringBuilder.append(Integer.toHexString(random.nextInt(16)));
    }
    return stringBuilder.toString();
}


}

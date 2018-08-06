package net.zembrowski.julian.domain;


public class Para {
  private int liczba;

  private String nazwa;

    public int getLiczba() {
        return liczba;
    }

    public void setLiczba(int liczba) {
        this.liczba = liczba;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

   public String translateDay()
  {
      switch (nazwa) {
          case "TUESDAY":
              return "wtorek";
          case "WEDNESDAY":
              return "środa";
          case "THURSDAY":
              return "czwartek";
          case "FRIDAY":
              return "piątek";
          case "SATURDAY" :
              return "sobota";
          case "SUNDAY" :
              return "niedziela";
          case "MONDAY":
              return "poniedziałek";
              default:
                  return "blad";
      }
  }

}

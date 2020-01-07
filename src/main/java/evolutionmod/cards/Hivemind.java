package evolutionmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.AdaptDronesAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class Hivemind
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Hivemind";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int MAX_DRONES_AMT = 2;
    private static final int UPGRADE_MAX_DRONES_AMT = 1;

    public Hivemind() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = MAX_DRONES_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.orbs.stream()
                .filter(o -> o instanceof InsectGene)
                .limit(this.magicNumber)
                .forEach(o -> this.addAdaptation(((AbstractGene) o).getAdaptation()));
        this.useAdaptations(p, m);
        AbstractDungeon.actionManager.addToBottom(
                new AdaptDronesAction(p, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_MAX_DRONES_AMT);
        }
    }
}
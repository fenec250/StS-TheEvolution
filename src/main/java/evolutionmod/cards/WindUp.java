package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.patches.AbstractCardEnum;

public class WindUp
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:WindUp";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int DRAW_AMT = 2;
    private static final int UPGRADED_DRAW_AMT = 1;

    public WindUp() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRAW_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        p.orbs.stream()
                .filter(o -> o instanceof CentaurGene)
                .limit(1)
                .forEach(o -> this.addAdaptation(((AbstractGene) o).getAdaptation()));
        this.useAdaptations(p, m);
    }

    @Override
    public int addAdaptation(AbstractAdaptation adaptation) {
        if (!adaptation.getGeneId().equals(CentaurGene.ID)) {
            return 0;
        }
        int amountLeft = this.magicNumber;
        if (this.adaptationMap.containsKey(CentaurGene.ID)) {
            amountLeft -= this.adaptationMap.get(CentaurGene.ID).amount;
        }
        adaptation.amount = Math.min(adaptation.amount, amountLeft);
        return super.addAdaptation(adaptation);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_DRAW_AMT);
        }
    }
}
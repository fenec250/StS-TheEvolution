package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;

public class Hivemind
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:Hivemind";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectSkl.png";
    private static final int COST = 1;
    private static final int DRONES_AMT = 2;
    private static final int UPGRADE_DRONES_AMT = 1;
    private static final int MAX_ADAPT_AMT = 2;

    public Hivemind() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRONES_AMT;
        this.adaptationMap.put(InsectGene.ID, new InsectGene.Adaptation(0, MAX_ADAPT_AMT));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p), this.magicNumber));
        p.orbs.stream()
                .filter(o -> this.canAdaptWith(o) > 0)
                .findAny()
                .ifPresent(o -> this.tryAdaptingWith((AbstractGene) o, true));
        this.useAdaptations(p, m);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRONES_AMT);
        }
    }
}
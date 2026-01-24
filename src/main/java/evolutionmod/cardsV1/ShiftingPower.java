package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.EvolutionEnum;

import java.util.List;
import java.util.stream.Collectors;

public class ShiftingPower
        extends BaseEvoCard {
    public static final String cardID = "ShiftingPower";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/ShiftingPower.png";
    private static final int COST = 0;
    private static final int GENE_TRIGGER_AMT = 1;
    private static final int UPGRADE_GENE_TRIGGER_AMT = 1;

    public ShiftingPower() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = GENE_TRIGGER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        List<AbstractOrb> triggered = p.orbs.stream()
                .filter(o -> o instanceof AbstractGene)
                .limit(this.magicNumber)
                .collect(Collectors.toList());

        triggered.forEach(o -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ((AbstractGene)o).getAdaptation().apply(p, m);
                this.isDone = true;
            }
        }));

        List<AbstractOrb> shifted = p.orbs.stream()
                .filter(o -> o instanceof AbstractGene)
                .limit(this.energyOnUse)
                .collect(Collectors.toList());

        shifted.forEach(o -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                consumeOrb(p, o);
                addToTop(new ChannelAction(o));
                this.isDone = true;
            }
        }));
//        addToBot(new RefundAction(this, this.energyOnUse, this.energyOnUse));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShiftingPower();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_GENE_TRIGGER_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}
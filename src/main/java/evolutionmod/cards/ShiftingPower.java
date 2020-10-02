package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;
import java.util.stream.Collectors;

public class ShiftingPower
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:ShiftingPower";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int DRAW_AMT = 1;
    private static final int GENE_TRIGGER_AMT = 2;
    private static final int UPGRADE_GENE_TRIGGER_AMT = 1;

    public ShiftingPower() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.draw = this.baseDraw = DRAW_AMT;
        this.magicNumber = this.baseMagicNumber = GENE_TRIGGER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.draw));
        List<AbstractOrb> triggered = p.orbs.stream()
                .filter(o -> o instanceof AbstractGene)
                .limit(this.magicNumber)
                .collect(Collectors.toList());
        triggered.forEach(o -> ((AbstractGene)o).getAdaptation().apply(p, m));
//                .ifPresent(o -> this.addAdaptation((AbstractGene) o));
//        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new InsectGene()));
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
        }
    }
}
package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.MarkPower;

public class ChannelMagic
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:ChannelMagic";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int MARK_AMT = 1;
    private static final int UPGRADE_MARK_AMT = 1;
    private static final int FORM_AMT = 1;
    private static final int MAX_ADAPT_AMT = 2;
    private static final int UPGRADE_MAX_ADAPT_AMT = 1;

    public ChannelMagic() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = FORM_AMT;
        this.maxAdaptationMap.put(LavafolkGene.ID, MAX_ADAPT_AMT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                m, p, new MarkPower(m, this.magicNumber), this.magicNumber
        ));
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
            this.upgradeMagicNumber(UPGRADE_MARK_AMT);
            this.upgradeAdaptationMaximum(LavafolkGene.ID, UPGRADE_MAX_ADAPT_AMT);
        }
    }
}